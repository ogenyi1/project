package ng.optisoft.rosabon.dto.request;

import lombok.Data;

@Data
public class AccountOfficerRequest
{
	private String branchName;
	private String departmentName;
	private Long userId;
	private String staffName;
}
