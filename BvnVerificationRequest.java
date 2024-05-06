package ng.optisoft.rosabon.dto.request;

import java.time.LocalDate;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import ng.optisoft.rosabon.constant.DateTimeEnum;
import ng.optisoft.rosabon.constant.MessageConstant;

@Data
public class BvnVerificationRequest
{
	@NotEmpty(message = MessageConstant.BVN_REQUIRED)
	@Positive(message = MessageConstant.INVALID_BVN_DIGIT)
	@Size(min=11, max=11, message = MessageConstant.INVALID_BVN_DIGIT)
	private String id;
	
	@NotNull(message = MessageConstant.SUBJECT_CONSENT_REQUIRED)
	@AssertTrue(message = MessageConstant.MUST_BE_TRUE)
	private Boolean isSubjectConsent;
	
	@NotEmpty(message = MessageConstant.PHONE_NUMBER_EMPTY)
	@Size(min=10, max=15, message = MessageConstant.INVALID_PHONE_NUMBER)
	private String phoneNumber;
	
	private String firstName;
	
	private String lastName;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeEnum.DATE_PATTERN2)
	private LocalDate dateOfBirth;
}
