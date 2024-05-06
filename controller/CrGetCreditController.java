package ng.optisoft.rosabon.credit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.credit.dto.request.*;
import ng.optisoft.rosabon.credit.enums.AssetTypeLease;
import ng.optisoft.rosabon.credit.externalapis.erp.BasicErpService;
import ng.optisoft.rosabon.credit.externalapis.erp.pojo.DocumentChecklistResponse;
import ng.optisoft.rosabon.credit.service.CreditUserService;
import ng.optisoft.rosabon.dto.HttpResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@RestController
@CrossOrigin
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR', 'INDIVIDUAL_USER', 'COMPANY')")
@Tag(name = "cr-get-credit-controller")
@Slf4j
public class CrGetCreditController {
    private final CreditUserService creditUserService;
    private final BasicErpService basicErpService;


    @PostMapping(value = ApiRoute.USERS + ApiRoute.INITIALIZE_LOAN)
    public ResponseEntity<?> initializeLoanProcessing(@RequestBody LoanProcessingRequest request, Authentication authentication) {

        return creditUserService.startLoanProcessing(authentication, request);
    }

    @PostMapping(value = ApiRoute.USERS + ApiRoute.ACCEPT_LOAN_OFFER)
    public ResponseEntity<?> acceptLoanOffer(@RequestBody LoanOfferAcceptanceReq request, Authentication authentication) {

        return creditUserService.acceptLoanOffer(authentication, request);
    }

    @PostMapping(value = ApiRoute.USERS + ApiRoute.GET_LOAN_VALUE)
    public ResponseEntity<?> getLoanValue(@RequestBody LoanValueRequest request, Authentication authentication) {

        return creditUserService.getLoanValue(authentication, request);
    }


    @PostMapping(value = ApiRoute.USERS + ApiRoute.GENERATE_REPAYMENT)
    public ResponseEntity<?> generateRepayment(@RequestBody GenerateRepaymentRequest request, Authentication authentication) {

        return creditUserService.generateRepayment(authentication, request);
    }

    @PostMapping(value = ApiRoute.USERS + ApiRoute.COMPLETE_REPAYMENT_SETUP)
    public ResponseEntity<?> completeRepaymentSetUp(@RequestBody RepaymentOptionRequest request, Authentication authentication) {

        return creditUserService.completeRepayment(authentication, request);
    }

    @PostMapping(value = ApiRoute.USERS + ApiRoute.SUBMIT_LOAN_PROCESSING)
    public ResponseEntity<?> submitLoanForProcessing(@RequestBody SubmitLoanRequest request, Authentication authentication) {

        return creditUserService.submitLoanForProcessing(authentication, request);
    }

    @PostMapping(value = ApiRoute.USERS + ApiRoute.MAKE_BANK_STATEMENT_CALL)
    @Operation(
            summary = "Extract and analyze bank statement",
            description = "NB: The following documentation applies to semi-automated loans only until further advised." +
                    " The default tool for bank statement analysis MBS has low coverage in terms of the banks it supports." +
                    " A check will be done when this endpoint is called to determine if the user's bank is supported by MBS." +
                    " If it is  not, the value of the message field in the response of this endpoint," +
                    " will be used to determine whether to use the Mono connect SDK along with the sequence of endpoints within the CrMonoNotificationController to" +
                    " extract and analyze the user's bank statement." +
                    " If the message field is 'Redirecting to MONO'," +
                    " This will be the queue for the WEB and MOBILE devs to initiate the mono flow using the relevant SDK." +
                    " Once done with the CrMonoNotificationController flow, the flow of the GetCredit module is to be continued as prescribed in the PRD and figma designs"
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponseDto.class)))
    public ResponseEntity<HttpResponseDto> makeBankStatementCall(@RequestBody BankStatementRequestDto request, Authentication authentication) {
        ResponseEntity<?> response = creditUserService.extractAndAnalyzeBankStatement(authentication, request);
        return new ResponseEntity<>((HttpResponseDto) response.getBody(),response.getStatusCode());//for swagger documentation
    }

    @PostMapping(value = ApiRoute.USERS + ApiRoute.UPLOAD_LOAN_REQUIREMENT)
    public ResponseEntity<?> uploadLoanRequirement(@RequestBody UploadRequirementRequest request, Authentication authentication) {

        return creditUserService.uploadRequirement(authentication, request);
    }

    @GetMapping(value = ApiRoute.USERS + "/{transactionId}" +  ApiRoute.ERP_PRODUCT_DOCUMENT_CHECKLIST)
    @Operation(
            summary = "Use this endpoint to get list of required documents",
            description  = "Get all required documents for thw loan that was submitted for processing, use the transaction Id generated in the previous endpoint to fetch the list of required documents"

    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class)))
    public ResponseEntity<?> getDocumentChecklist(@PathVariable String transactionId) {
        List<DocumentChecklistResponse> response = basicErpService.handleCheckingDocumentChecklist(transactionId);
        if(response != null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
    }

//    @PostMapping(ApiRoute.USERS + "/analyze-bank-statement")
//    public ResponseEntity<?> analyzeBankStatement(@RequestBody StatementAnalysisReq req) {
//        return creditUserService.analyzeBankStatement(req);
//    }



}
