package ng.optisoft.rosabon.dto.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class BusinessDetailInDto
{
	private String businessType;
	private String name;
	private String registrationNumber;
	private LocalDate dateOfRegisteration;
	private Long countryId;
	private String address;
	private String city;
	private Long stateId;
	private Long lgaId;
}
