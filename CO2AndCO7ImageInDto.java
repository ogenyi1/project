package ng.optisoft.rosabon.dto.request;

import lombok.Data;
import ng.optisoft.rosabon.constant.MessageConstant;

import javax.validation.constraints.NotBlank;

@Data
public class CO2AndCO7ImageInDto {
    private String name;

    @NotBlank(message = MessageConstant.ENCODED_UPLOAD_REQUIRED)
    private String encodedUpload;
}