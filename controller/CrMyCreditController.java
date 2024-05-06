package ng.optisoft.rosabon.credit.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.credit.dto.request.PayOffLoanReq;
import ng.optisoft.rosabon.credit.dto.request.RepayLoanReq;
import ng.optisoft.rosabon.credit.dto.request.SubmitChecklistItemsReq;
import ng.optisoft.rosabon.credit.service.CrMyCreditService;
import ng.optisoft.rosabon.dto.response.HttpResponseDto2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiRoute.AUTH + ApiRoute.SEPARATOR + "my-credit")
public class CrMyCreditController {

    private final CrMyCreditService myCreditService;

    @GetMapping(ApiRoute.TRANSACTIONS + ApiRoute.SEPARATOR + "active")
    @ApiOperation("Get active transactions")
    public ResponseEntity<?> getActiveTransactions(@RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                   @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir) {
        return myCreditService.getActiveTransactions(
                pageNo,
                pageSize,
                sortDir
        );
    }

    @GetMapping(ApiRoute.TRANSACTIONS + ApiRoute.SEPARATOR + "pending")
    @ApiOperation("Get pending transactions")
    public ResponseEntity<?> getPendingTransactions(@RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                    @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir) {
        return myCreditService.getPendingTransactions(
                pageNo,
                pageSize,
                sortDir
        );
    }

    @GetMapping(ApiRoute.TRANSACTIONS + ApiRoute.SEPARATOR + "closed")
    @ApiOperation("Get closed transactions")
    public ResponseEntity<?> getClosedTransactions(@RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                   @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir) {
        return myCreditService.getClosedTransactions(pageNo, pageSize, sortDir);
    }

    @GetMapping(ApiRoute.TRANSACTIONS + ApiRoute.SEPARATOR + "{id}")
    @ApiOperation("Get transaction")
    public ResponseEntity<?> getTransaction(@PathVariable("id") String loanId) {
        return myCreditService.getTransaction(loanId);
    }

    @GetMapping(ApiRoute.TRANSACTIONS + ApiRoute.SEPARATOR + "/get-pending-transaction-details/{id}")
    @ApiOperation("Get pending transaction details")
    public ResponseEntity<?> getPendingTransactionDetails(@PathVariable("id") Long loanId) {
        return myCreditService.getPendingTransactionDetails(loanId);
    }

    @PostMapping(ApiRoute.SEPARATOR + "withdraw-loan-request/{transaction-id}")
    @ApiOperation("Withdraw loan request for a transaction submitted to ERP")
    public ResponseEntity<?> withdrawLoanRequest(@PathVariable("transaction-id") String transactionId) {
        return myCreditService.withdrawLoanRequest(transactionId);
    }

    @GetMapping(ApiRoute.TRANSACTIONS + ApiRoute.SEPARATOR + "required-docs" + ApiRoute.SEPARATOR + "{transaction-id}")
    @ApiOperation("Get underwriter docs for a transaction submitted to ERP")
    public ResponseEntity<?> getRequiredUnderwriterDocs(@PathVariable("transaction-id") String transactionId) {
        return myCreditService.getRequiredUnderwriterDocs(transactionId);
    }

    @PostMapping(ApiRoute.TRANSACTIONS + ApiRoute.SEPARATOR + "submit-underwriter-docs")
    @ApiOperation("Submit requested underwriter documents")
    public ResponseEntity<HttpResponseDto2> submitUnderwriterDocs(@RequestBody SubmitChecklistItemsReq submitChecklistItemsReq) {
        return myCreditService.submitUnderwriterDocs(submitChecklistItemsReq);
    }

//    @PostMapping(ApiRoute.TRANSACTIONS + ApiRoute.SEPARATOR + "{transaction-id}" + ApiRoute.SEPARATOR + "top-up-loan")
//    @ApiOperation("Top up an existing loan")
//    public ResponseEntity<?> topUpExistingLoan (@PathVariable("transaction-id") Long transactionId, @RequestBody @Valid TopUpLoanReq topUpLoanReq) {
//        return myCreditService.topUpExistingLoan(transactionId,topUpLoanReq);
//    }
//
    @PostMapping(ApiRoute.TRANSACTIONS + ApiRoute.SEPARATOR + "{transaction-id}" + ApiRoute.SEPARATOR + "repay-part-of-loan")
    @Operation(
            summary = "Repay part of an existing loan",
            description = ""
    )
    public ResponseEntity<HttpResponseDto2> repayPartOfExistingLoan (@PathVariable("transaction-id") String transactionId,
                                                @RequestBody @Valid RepayLoanReq repayLoanReq) {
        return myCreditService.repayPartOfExistingLoan(transactionId,repayLoanReq);
    }
    @PostMapping(ApiRoute.TRANSACTIONS + ApiRoute.SEPARATOR + "{transaction-id}" + ApiRoute.SEPARATOR + "pay-off-loan")
    @Operation(
            summary = "Pay off an existing loan",
            description = ""
    )
    public ResponseEntity<HttpResponseDto2> payOffExistingLoan (@PathVariable("transaction-id") String transactionId,
                                                                @RequestBody @Valid PayOffLoanReq payOffLoanReq) {
        return myCreditService.payOffExistingLoan(transactionId,payOffLoanReq);
    }

    @PostMapping(ApiRoute.TRANSACTIONS + ApiRoute.SEPARATOR + "{transaction-id}" + ApiRoute.SEPARATOR + "get-repayment-history")
    @Operation(
            summary = "Get loan repayment history",
            description = ""
    )
    public ResponseEntity<HttpResponseDto2> getLoanRepaymentHistory(@RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                                    @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
                                                                    @PathVariable("transaction-id") String transactionId) {
        return myCreditService.getLoanRepaymentHistory(
                pageNo,
                pageSize,
                sortDir,
                transactionId
        );
    }

    @GetMapping(ApiRoute.TRANSACTIONS + ApiRoute.SEPARATOR + ApiRoute.RUNNING_TRAN)
    @ApiOperation("Get the currently running loan for the login user")
    public ResponseEntity<HttpResponseDto2> getRunningLoanForCurrentUser() {
        return myCreditService.getCurrentUserActiveLoan();
    }

}
