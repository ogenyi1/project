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
public class ProvidusBlackListAccountRequestDto {
    @ApiModelProperty(name = "account name", allowEmptyValue = false, required = true, example = "John Elesho")
    @NotNull
    @NotEmpty(message = APIMessage.REQUIRED_FULLNAME)
    private String account_name;

    @ApiModelProperty(name = "BVN", allowEmptyValue = true, required = false, example = "1")
    private int flag = 1;
}
