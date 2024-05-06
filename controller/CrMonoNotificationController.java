package ng.optisoft.rosabon.credit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.credit.dto.response.GenericResponse;
import ng.optisoft.rosabon.credit.externalapis.mono.BankMonoStatementService;
import ng.optisoft.rosabon.credit.externalapis.mono.dtos.request.MonoExchangeTokenReq;
import ng.optisoft.rosabon.credit.externalapis.mono.statementcall.MonoConnectSessionRequest;
import ng.optisoft.rosabon.credit.externalapis.mono.statementcall.MonoFinInstitutionLoginRequest;
import ng.optisoft.rosabon.credit.externalapis.mono.statementcall.MonoRecommitSessionRequest;
import ng.optisoft.rosabon.credit.externalapis.mono.webhook.MonoWebNotificationReq;
import ng.optisoft.rosabon.dto.response.HttpResponseDto2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping(ApiRoute.SEPARATOR + "mono")
@Slf4j
@Tag(name = "cr-mono-notification-controller", description = "Welcome to the Mono Controller. " +
        "These set of endpoints are to be used when account statement call up is not supported with the primary tool which is MBS. " +
        "This will be determined by the frontend developer using the response gotten from the call to the extractAndAnalyzeBankStatement() endpoint under Cr-get-credit-controller. " +
        "Please follow the ordering of the endpoints provided here till the last step. " +
        "PLEASE NOTE : A few of the first steps will have to be executed using the Mono SDK (unless otherwise advised) after which the account statement " +
        "(json or base64 encoded pdf depending on the product in question) which is gotten at step 7 will be submitted to the analyzeBankStatementEndpoint() " +
        "in the Cr-get-credit-controller to continue the initial loan booking process." +
        "These Apis have been built with the belief that the front-end developer will be able keep track of the product type e.g QUICK NANO or CASH BACKED LOAN and the " +
        "loan processing stage and use these two values to determine the order in which to " +
        "1. call the endpoints in the Cr-get-credit-controller, " +
        "2. integrate the flow of these endpoints into the getCredit module at the point when bank statement call up is required. " +
        "3. continue with the flow of getCredit module depending on the product" +
        "This is very key to ensuring a seamless flow. Good luck!!!")
public class CrMonoNotificationController {

    private final BankMonoStatementService monoStatementService;
    @PostMapping(value = ApiRoute.SEPARATOR + "8" + ApiRoute.GET_MONO_WEB_HOOK_NOTIFICATION)
    @Operation(
            summary = "8. Receive mono webhook",
            description = "While the front-end and mobile developers will not be required to integrate this endpoint directly, " +
                    "It will be beneficial to understand its purpose as the information processed here will be crucial to how the reauthorizeMonoAccount() endpoint in step 9 " +
                    "will be integrated. This webhook receives automated datasync from the mono API about the status of a connected account's financial data. " +
                    "This helps ensure that information such as bank statement is as up to date as possible when requests for the account. " +
                    "Webhook for all connected accounts is received once every 24hrs. The frequency can be increased upon request to Mono." +
                    "If a webhook event called mono.events.account_updated has been received, requests to the Information , Statement, Transactions , " +
                    "Income endpoints on the Mono API will now return new data. " +
                    "If the connected account has Multi-Factor Authentication (MFA) enabled, the user must re-authorise the connection using MFA " +
                    "(i.e OTP, security question or CAPTCHA), before new data can be retrieved from their financial institution. " +
                    "If this is the case, a webhook event called mono.events.reauthorisation_required will received here. " +
                    "The account id will be flagged in the database as requiring reauthorization." +
                    "At this point, the frontend/mobile developer will be expected to check for the requiresMonoReauth boolean field on the response object at login. " +
                    "They will use this to determine whether user is to be prompted to reauthorize via the mono widget/SDK. " +
                    "If true, a call will need to be made to the reauthorizeMonoAccount() endpoint in step 9 using the monoAccountId field which is also returned " +
                    "in the login response body. This will return a json object with field of token which you can pass to the mono connect widget's reauthorise() " +
                    "function which will prompt the user to submit the required input. " +
                    "Please note that connect.reauthorise(reauthToken), replaces connect.setup() in the Mono re-authorisation widget. " +
                    "Once the user has successfully completed the re-authorisation, a webhook is received with status mono.events.account_updated to notify the system that the new data is available. " +
                    "It is strongly recommended that reauthorization is made mandatory at login if required in order to avoid users processing loans with outdated financial data"
    )
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    public ResponseEntity<?> getWebHookNotification(@RequestBody MonoWebNotificationReq request) {
        log.info("================MONO WEBHOOK INVOKED=====================:  " + request);
        return monoStatementService.getWebHookNotification(request);
//        return ResponseEntity.ok(null);
    }

    @GetMapping(value =ApiRoute.SEPARATOR + "1" + ApiRoute.SEPARATOR + ApiRoute.GET_FINAN_INSTITUTION)
    @Operation(
            summary = "1. Get a list of available financial institutions from the mono API",
            description = "This is the first step in implementing the Mono Connect White Label API is to get the list of all the supported financial institutions.\n" +
                    " This is the first in a sequence of endpoints to be called in order to generate account id for bank statement call up.\n" +
                    " In the response of this call, _id refers to the institution ID,\n" +
                    " while the auth_methods array contains the different authentication method types\n" +
                    " and the UI object available for a financial institution.\n" +
                    " These two parameters, alongside the app ID which you can find on the Apps page on your Mono dashboard,\n" +
                    " will be used in the next endpoint to create/start a Mono-Connect session." +
                    " Loan id will need to be passed as request parameter to keep track of the loan being processed." +
                    " This will be returned in the responsePayload object of the response so it can be tracked throughout the flow"
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponse.class)))
    public ResponseEntity<GenericResponse> fetchFinancialInstitutions(@RequestParam Long loanId) {
        var mono = monoStatementService.handleFetchAllFinancialInstitution(loanId);
        return ResponseEntity.ok(mono);
    }

    @PostMapping(value = ApiRoute.SEPARATOR + "2" + ApiRoute.SEPARATOR + ApiRoute.CONNECT_MONO_SESSION)
    @Operation(
            summary = "2. Start a mono session",
            description = "Call this endpoint after  making use of the fetchFinancialInstitutions() endpoint in step 1.\n" +
                    " Pass a json request body containing the institution ID," +
                    " auth_method,and app ID gotten in the response body of step 1. " +
                    "Ensure that you pass the mono-sec-key (secret key) in the headers for a successful request to be made.\n" +
                    " NB: This will be handled from the backend server."+
                    " Loan id will need to be passed as request parameter to keep track of the loan being processed." +
                    " This will be returned in the responsePayload object of the response so it can be tracked throughout the flow"
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponse.class)))
    public ResponseEntity<GenericResponse> connectSession(@RequestBody MonoConnectSessionRequest request, @RequestParam Long loanId) {
        var mono = monoStatementService.connectMonoSession(request,loanId);
        return ResponseEntity.ok(mono);
    }

    @PostMapping(ApiRoute.SEPARATOR + "3" + ApiRoute.SEPARATOR + "log-user-into-financial-institution")
    @Operation(
            summary = "3. Log user in to his/her selected financial institution",
            description = "To log in, you need to get the x-session-id returned from the response of the connectSession() call in step two and pass it in the headers along with the mono-sec-key.\n" +
                    " Afterward, make a POST request to this login endpoint with the user’s credentials which he/she would have inputed into the form of the response from step 2.\n" +
                    " After this is done, depending on the response  of the call,\n" +
                    " we may need to reauthorize the user by calling the recommitSession() endpoint.\n" +
                    " It is important to note that an institution might need multiple levels of authorization flow before granting user access. For this reason,\n" +
                    " the next steps for the authorization flow are determined by the responseCode received from the login response in step three (the current step).\n" +
                    "If the status returned is 200, you will obtain a response code of either 99, 101, or 102.\n" +
                    "'99' indicates that the user has successfully signed in. You will be given a temporary authorization code,\n" +
                    " which you can use to access your Account ID through our Exchange token endpoint.\n" +
                    "There will be no need to recommit the session\n" +
                    "You can proceeded to call the exchange token endpoint.\n" +
                    " '101' indicates that your user has several accounts.\n" +
                    " The user would be required to pick the desired account to be enrolled.\n" +
                    " An array containing all of the discovered accounts will be returned in the response.\n" +
                    "'102' indicates that an input is required from the user to proceed, this may be a security question, OTP, token, etc." +
                    " Loan id will need to be passed as request parameter to keep track of the loan being processed." +
                    " This will be returned in the data object of the response so it can be tracked throughout the flow"
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponseDto2.class)))
    public ResponseEntity<HttpResponseDto2> logUserIntoFinInstitution(@RequestParam String sessionId, @RequestBody @Valid MonoFinInstitutionLoginRequest loginReq, @RequestParam Long loanId) {
        return monoStatementService.logUserIntoFinInstitution(sessionId,loginReq,loanId);
    }

    @PostMapping(ApiRoute.SEPARATOR + "4" + ApiRoute.SEPARATOR + "recommit-session")
    @Operation(summary = "4. Recommit/Reauthorize user session",
            description = "This is the final step in the authorization flow if a user’s input is required during the process (e.g account selection, OTP, security answer).\n" +
                    "Also, before you make a POST request to the endpoint, ensure you pass the x-session-id and mono-sec-key in the headers.\n" +
                    "The request body parameter could be either one of the following (account, answer, OTP) depending on the response code received after a successful login attempt.\n" +
                    "In case a session does not need to be committed and a commit request is made. the server will respond with 400 Bad Request response.\n" +
                    "Once the code has been retrieved in the authorization flow,\n" +
                    " proceed to the Exchange token endpoint to request an Account ID that will be used in making requests to Mono financial data endpoints." +
                    " Loan id will need to be passed as request parameter to keep track of the loan being processed." +
                    " This will be returned in the data object of the response so it can be tracked throughout the flow"
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponseDto2.class)))
    public ResponseEntity<HttpResponseDto2> recommitSession (@RequestParam String sessionId, @RequestBody @Valid MonoRecommitSessionRequest monoRecommitSessReq, @RequestParam Long loanId) {
        return monoStatementService.recommitSession(sessionId,monoRecommitSessReq,loanId);
    }

    @PostMapping(ApiRoute.SEPARATOR + "5" + ApiRoute.SEPARATOR + "exchange-token")
    @Operation(summary = "5. Generate/Fetch mono account id",
            description = "This endpoint should only be called after user has been fully authorized by either\n" +
                    " the recommitSession() endpoint or the logUserIntoFinInstitution() endpoint.\n" +
                    "The request body will be a json object having the single field of code gotten from either one of the aforementioned endpoints\n" +
                    "The response body will be an object with the single field of id signifying the account id of the connected bank account.\n" +
                    "If the bank account already has an id previously generated,that will be returned.\n" +
                    "If not, one will be generated, saved to the bank account and returned\n" +
                    "Once this is done, we can proceed to get account details in order to determine whether the data is available for lookup via" +
                    "the fetchAccountDetails() endpoint." +
//                    "Once this is done, we can proceed to get account statement for the account using the fetchAccountStatement() endpoint" +
                    " Loan id will need to be passed as request parameter to keep track of the loan being processed." +
                    " This will be returned in the data object of the response so it can be tracked throughout the flow"
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponseDto2.class)))
    public ResponseEntity<HttpResponseDto2> exchangeToken (@RequestBody @Valid MonoExchangeTokenReq exchangeTokenReq, @RequestParam Long loanId) {
        return monoStatementService.exchangeToken(exchangeTokenReq,loanId);
    }

    @PostMapping(ApiRoute.SEPARATOR + "6" + ApiRoute.SEPARATOR + "fetch-account-details")
    @Operation(
            summary = "6. Fetch account details",
            description = "This resource represents the account details with the financial institution." +
                    "At this stage, it is important to verify the data availability of your connected account to be available via the data_status field," +
                    " before going ahead to call the desired financial API endpoints for data." +
                    " Other possible values that can be received in the data status are AVAILABLE, PROCESSING or FAILED." +
                    "If the meta data_status is still in the processing stage," +
                    " you have to wait before calling the APIs below e.g Statement, Transaction, Income and Identity Endpoints." +
                    " Some banks are faster than others," +
                    " so it may be available instantly after authorization and some banks might take a few seconds or minutes." +
                    "Please note that if you proceed to call our Financial APIs (e.g Transactions, Statements etc) without confirming your data status as AVAILABLE," +
                    " you will receive an empty payload in your API response." +
                    "If data status is returned as AVAILABLE, we can proceed to call the fetchBankStatement() endpoint" +
                    "If data status is returned as PROCESSING, we can proceed to display a message to the user" +
                    " indicating that their banking data is still being processed and they should try the process again in a couple of minutes. " +
                    "If data status is returned as FAILED, we can proceed to display a message to the user" +
                    "that fetching their data from the financial institution failed and prompt them to retry." +
                    " Loan id will need to be passed as request parameter to keep track of the loan being processed." +
                    " This will be returned in the data object of the response so it can be tracked throughout the flow"
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponseDto2.class)))
    public ResponseEntity<HttpResponseDto2> fetchAccountDetails (@RequestParam String accountId, @RequestParam Long loanId) {
        return monoStatementService.fetchAccountDetails(accountId,loanId);
    }
    @PostMapping(ApiRoute.SEPARATOR + "7" + ApiRoute.SEPARATOR + "fetch-account-statement")
    @Operation(
            summary = "7. Fetch account statement with mono",
            description = "User must have a mono account id saved against their bank account within the system prior to calling this endpoint.\n" +
                    "This resource represents the bank statement of the connected financial account.\n" +
                    "You can query 1-12 months bank statement in one single call.\n" +
                    "The default period configured will be 6 months" +
                    "You can specify a period by passing the numerical value (e.g. 6) of the number of months as the request parameter named period.\n" +
                    " Loan id will need to be passed as request parameter to keep track of the loan being processed." +
                    " This will be returned in the data object of the response so it can be tracked throughout the flow"
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponseDto2.class)))
    public ResponseEntity<HttpResponseDto2> fetchJsonOrPdfAccountStatement (@RequestParam(defaultValue = "6") @Min(1) @Max(12) int period,
                                                    @RequestParam(defaultValue = "false") boolean usePDF,
                                                    @RequestParam Long loanId) {
        return monoStatementService.fetchJsonOrPdfAccountStatement(period,usePDF,loanId);
    }

    @Operation(
            summary = "9. Reauthorize account with mono",
            description = "As detailed in step 8, this endpoint should be called when reauthorization is required at login. " +
                    "The token gotten from the response should be passed to the mono connect widget's reauthorise(). " +
                    "function which will prompt the user to submit the required input. " +
                    "Please note that connect.reauthorise(reauthToken), replaces connect.setup() in the Mono re-authorisation widget. " +
                    "Once the user has successfully completed the re-authorisation, a webhook is received with status mono.events.account_updated to notify the system that the new data is available. " +
                    "It is strongly recommended that reauthorization is made mandatory at login if required in order to avoid users processing loans with outdated financial data"
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponseDto2.class)))
    @PostMapping(ApiRoute.SEPARATOR + "9" + ApiRoute.SEPARATOR + "reauthorize-mono-account")
    public ResponseEntity<HttpResponseDto2> reauthorizeMonoAccount (@RequestParam String accountId) {
        return monoStatementService.reauthorizeMonoAccount(accountId);
    }

}
