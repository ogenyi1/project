package ng.optisoft.rosabon.dto.request;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AcceptOrRejectWithdrawalRequest
{
	public enum AcceptOrRejectWithdrawalStatus{APPROVE, REJECT}
	
	@NotNull(message = "Withdrawal Request is required")
	private Long withdrawalRequestId;
	
	@NotNull(message = " Status required")
	private AcceptOrRejectWithdrawalStatus status;
}
