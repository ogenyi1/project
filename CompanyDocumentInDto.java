package ng.optisoft.rosabon.dto.request;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import ng.optisoft.rosabon.constant.MessageConstant;

@Data
public class CompanyDocumentInDto
{
	@NotNull(message = MessageConstant.CERTIFICATE_OF_INCO_REQUIRED)
	@Valid
	private CertificateOfIncoImageInDto certificateOfIncoImage;
	
	@Valid
	private CacImageInDto cacImage;
	
	@Valid
	private MemorandumOfAssociationImageInDto moaImage;
	
	@NotNull(message = MessageConstant.CONTACT_PERSON_PHOTOGRAPH_REQUIRED)
	@Valid
	private ContactPersonPhotographImageInDto contactPersonPhotographImage;
	
	@NotNull(message = MessageConstant.ID_TYPE_REQUIRED)
	private Long idTypeId;
	
	@NotNull(message = MessageConstant.CONTACT_PERSON_IDENTITY_REQUIRED)
	@Valid
	private ContactPersonIdentificationImageInDto contactPersonIdentityImage;
	
	@NotBlank(message = MessageConstant.ID_NUMBER_REQUIRED)
	private String contactPersonIdNumber;
	
	@NotNull(message = MessageConstant.UTILITY_BILL_REQUIRED)
	@Valid
	private UtilityBillImageInDto utilityBillImage;
}
