package ng.optisoft.rosabon.integrations.erp;

import ng.optisoft.rosabon.dto.response.HttpResponseDto2;
import ng.optisoft.rosabon.integrations.erp.dto.request.ErpActionsReq;
import ng.optisoft.rosabon.integrations.erp.dto.request.UpdateLoanRepaymentReq;
import ng.optisoft.rosabon.integrations.erp.dto.response.ErpActionsResponse;
import org.springframework.http.ResponseEntity;

public interface ErpService {
    ResponseEntity<HttpResponseDto2> receiveRepaymentInfo(UpdateLoanRepaymentReq updateLoanRepaymentReq);

    ResponseEntity<HttpResponseDto2> withdrawLoanRequest(String transactionId);

    ResponseEntity<ErpActionsResponse> receiveErpActions(ErpActionsReq erpActionsReq);


    ResponseEntity<HttpResponseDto2> sendRepaymentInfo(UpdateLoanRepaymentReq updateLoanRepaymentReq);
}
