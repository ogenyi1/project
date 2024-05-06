package ng.optisoft.rosabon.integrations.providus.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ng.optisoft.rosabon.constant.APIMessage;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProvidusReservedAccountRequestDto {
    @ApiModelProperty(name = "account name", allowEmptyValue = false, required = true, example = "John Elesho")
    @NotNull
    @NotEmpty(message = APIMessage.REQUIRED_FULLNAME)
    private String account_name;

    @ApiModelProperty(name = "BVN", allowEmptyValue = false, required = true, example = "2222101910")
    @NotNull
    @NotEmpty(message = APIMessage.REQUIRED_BVN)
    private String bvn;
}
