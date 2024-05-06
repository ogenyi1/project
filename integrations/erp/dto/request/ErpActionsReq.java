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
public class ErpActionsReq {
    @JsonProperty("TransactionID")
    private String transactionID;
    @JsonProperty("Action")
    private String action;
    @JsonProperty("PushReason")
    private String pushReason;
    @JsonProperty("NextBookingStage")
    private String nextBookingStage;

    @Override
    public String toString() {
        return ToStringUtil.print(this);
    }
}
