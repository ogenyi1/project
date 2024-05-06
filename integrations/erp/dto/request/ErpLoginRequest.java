package ng.optisoft.rosabon.integrations.erp.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import ng.optisoft.rosabon.util.ToStringUtil;

@Builder
//@ToString
public class ErpLoginRequest {
    @JsonProperty("Username")
    private String username;
    @JsonProperty("Password")
    private String password;

    @Override
    public String toString() {
        return ToStringUtil.print(this);
    }
}
