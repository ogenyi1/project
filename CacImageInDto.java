package ng.optisoft.rosabon.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import ng.optisoft.rosabon.constant.MessageConstant;

@Data
public class CacImageInDto
{
	private String name;
	
	@NotBlank(message = MessageConstant.ENCODED_UPLOAD_REQUIRED)
	private String encodedUpload;
}
