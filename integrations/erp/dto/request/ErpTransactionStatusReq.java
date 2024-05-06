package ng.optisoft.rosabon.integrations.erp.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ng.optisoft.rosabon.util.ToStringUtil;

@Builder
@Data
public class ErpTransactionStatusReq {
    @JsonProperty("TransactionID")
    private Long transactionID;

    @Override
    public String toString() {
        return ToStringUtil.print(this);
    }
}
