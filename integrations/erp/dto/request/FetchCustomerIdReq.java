package ng.optisoft.rosabon.integrations.erp.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ng.optisoft.rosabon.util.ToStringUtil;

@Data
@Builder
public class FetchCustomerIdReq {
    @JsonProperty("FirstName")
    private String firstName;
    @JsonProperty("MiddleName")
    private String middleName;
    @JsonProperty("LastName")
    private String lastName;
    @JsonProperty("Gender")
    private String gender;
    @JsonProperty("DateOfBirth")
    private String dateOfBirth;
    @JsonProperty("PrimaryPhonenumber")
    private String primaryPhoneNumber;
    @JsonProperty("BVN")
    private String bVN;
    @JsonProperty("EmailAddress")
    private String emailAddress;
    @JsonProperty("CustomerType")
    private String customerType;

    @Override
    public String toString() {
        return ToStringUtil.print(this);
    }


}
