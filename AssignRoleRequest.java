package ng.optisoft.rosabon.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;
import ng.optisoft.rosabon.constant.MessageConstant;

@Data
public class AssignRoleRequest
{
	@NotEmpty(message = MessageConstant.ROLE_NAME_REQUIRED)
	private String roleName;
	
	@NotNull(message = MessageConstant.ASSIGNED_USER_ID_REQUIRED)
	private Long userId;
}
