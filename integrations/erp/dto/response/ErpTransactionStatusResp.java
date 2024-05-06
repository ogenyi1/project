package ng.optisoft.rosabon.integrations.erp.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ng.optisoft.rosabon.util.ToStringUtil;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ErpTransactionStatusResp {
    public enum ErpTransactionStatus {
        ACTIVE("Active"),
        APPROVED("Approved"),
        BLACKLISTED("Blacklisted"),
        CASHED_OUT("Cashed Out"),
        COMPLETED("Completed"),
        DELETED("Deleted"),
        DISBURSED("Disbursed"),
        NIL_OFF("Nil-Off"),
        PAID_OFF("Paid-Off"),
        PENDING("Pending"),
        REFUNDED("Refunded"),
        RESTRUCTURED("Restructured"),
        REVIEWED("Reviewed"),
        TERMINATED("Terminated"),
        UPDATED("Updated");

        private String name;

        ErpTransactionStatus (String name) {
            name = name;
        }
    }

    @JsonProperty("transactionID")
    private String transactionId;
    @JsonProperty("product_ID")
    private String productId;
    @JsonProperty("product_Name")
    private String productName;
    private String currentStep;
    private String status;

    @Override
    public String toString() {
        return ToStringUtil.print(this);
    }

}
