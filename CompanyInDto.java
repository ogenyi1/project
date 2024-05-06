package ng.optisoft.rosabon.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import ng.optisoft.rosabon.constant.DateTimeEnum;
import ng.optisoft.rosabon.model.Company.CompanyType;
import ng.optisoft.rosabon.util.CustomJsonSerializer;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
public class CompanyInDto {

	@NotEmpty(message = "Company name is required")
	private String name;

	@NotEmpty(message = "Contact Firstname is required")
	private String contactFirstName;

	private String contactMiddleName;

	@NotEmpty(message = "Contact Lastname is required")
	private String contactLastName;

//    @NotEmpty(message = "Company RC no is required")
	private String rcNumber;

//	private CreditCompanyUserType businessType;

	private String businessType;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeEnum.DATE_PATTERN)
	@JsonDeserialize(using = CustomJsonSerializer.class)
	private LocalDate contactDOB;

	private String contactPersonEmail;

	private String contactPersonPhoneNo;
	
//	@NotNull(message = MessageConstant.ADDRESS_DETAILS_REQUIRED)
	private String address;

//	private String businessType;

	private String dateOfInco;

	private String natureOfBusiness;

	private CompanyType companyType;

}
