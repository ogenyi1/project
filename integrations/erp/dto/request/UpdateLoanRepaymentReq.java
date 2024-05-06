package ng.optisoft.rosabon.integrations.erp.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ng.optisoft.rosabon.util.ToStringUtil;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UpdateLoanRepaymentReq {
    @JsonProperty("Amount")
    private String amount;
    @JsonProperty("TransactionID")
    private String transactionID;
    @JsonProperty("Datepaid")
    private String datePaid;

    @Override
    public String toString() {
        return ToStringUtil.print(this);
    }
}
