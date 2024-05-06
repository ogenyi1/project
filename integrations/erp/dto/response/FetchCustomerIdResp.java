package ng.optisoft.rosabon.integrations.erp.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class FetchCustomerIdResp {
    private String customerID;
    private String bvn;
    private String msg;
    private String type;
    private String title;
    private int status;
    private String traceId;
    private Object errors;

    @Override
    public String toString() {
        return ToStringUtil.print(this);
    }
}
