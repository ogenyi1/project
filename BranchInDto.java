package ng.optisoft.rosabon.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;
import ng.optisoft.rosabon.constant.MessageConstant;
import ng.optisoft.rosabon.model.Branch.Status;

@Data
public class BranchInDto
{
	@NotEmpty(message = MessageConstant.BRANCH_NAME_REQUIRED)
	private String name;
	private String description;
	private String updatedBy;
	@NotNull(message = MessageConstant.BRANCH_STATUS_REQUIRED)
	private Status status;
}
