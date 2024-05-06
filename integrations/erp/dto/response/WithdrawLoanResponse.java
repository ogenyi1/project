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
public class WithdrawLoanResponse {
    private String status;

    @JsonProperty("transactionID")
    private String transactionId;
    @Override
    public String toString() {
        return ToStringUtil.print(this);
    }
}
