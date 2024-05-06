package ng.optisoft.rosabon.dto.request;

import javax.validation.constraints.NotEmpty;

import lombok.Data;
import ng.optisoft.rosabon.constant.MessageConstant;

@Data
public class BankAccountRequest
{
	@NotEmpty(message = MessageConstant.BANK_CODE_REQUIRED)
	private String bankCode;
	
	@NotEmpty(message = MessageConstant.ACCOUNT_NUMBER_REQUIRED)
	private String accountNumber;
	
	@NotEmpty(message = MessageConstant.ACCOUNT_NAME_NOT_NULL)
	private String accountName;
}
