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
public class UpdateVirtualAccountNameDto {
    @ApiModelProperty(name = "account name", allowEmptyValue = false, required = true, example = "John Elesho")
    @NotNull
    @NotEmpty(message = APIMessage.REQUIRED_FULLNAME)
    private String account_name;

    @ApiModelProperty(name = "account number", allowEmptyValue = false, required = true, example = "9978733370")
    @NotNull
    @NotEmpty(message = APIMessage.REQUIRED_EMAIL)
    private String account_number;
}
