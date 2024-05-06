package ng.optisoft.rosabon.dto.request;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import ng.optisoft.rosabon.constant.MessageConstant;

@Data
public class BankAccountVerificationRequest
{
	@NotBlank(message = MessageConstant.ACCOUNT_NUMBER_REQUIRED)
	private String accountNumber;
	
	@NotBlank(message = MessageConstant.BANK_CODE_REQUIRED)
	private String bankCode;
	
	@AssertTrue(message = MessageConstant.MUST_BE_TRUE)
	private Boolean isSubjectConsent;
}
