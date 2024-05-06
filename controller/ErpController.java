package ng.optisoft.rosabon.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.integrations.erp.ErpService;
import ng.optisoft.rosabon.integrations.erp.dto.request.ErpActionsReq;
import ng.optisoft.rosabon.integrations.erp.dto.request.UpdateLoanRepaymentReq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiRoute.ERP)
@Slf4j
public class ErpController {

    private final ErpService erpService;

    @PostMapping(ApiRoute.SEPARATOR + "receive-repayment-info")
    @ApiOperation("Receive loan repayment info for a transaction from ERP")
    public ResponseEntity<?> receiveRepaymentInfo(@RequestBody UpdateLoanRepaymentReq updateLoanRepaymentReq) {
        return erpService.receiveRepaymentInfo(updateLoanRepaymentReq);
    }

    @PostMapping(ApiRoute.SEPARATOR + "withdraw-loan-request/{transaction-id}")
    @ApiOperation("Withdraw loan request for a transaction submitted to ERP")
    public ResponseEntity<?> withdrawLoanRequest(@PathVariable("transaction-id") String transactionId) {
        return erpService.withdrawLoanRequest(transactionId);
    }

    @PostMapping(ApiRoute.SEPARATOR + "receive-erp-actions")
    @ApiOperation("Receive ERP actions taken for a transaction submitted to ERP")
    public ResponseEntity<?> receiveErpActions(@RequestBody ErpActionsReq erpActionsReq) {
        return erpService.receiveErpActions(erpActionsReq);
    }


}
