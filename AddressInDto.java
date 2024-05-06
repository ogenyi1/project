package ng.optisoft.rosabon.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AddressInDto {

	private long id;

	@NotEmpty(message = "House address is required")
	private String houseNoAddress;

	private String streetAddress;

	private String homeAddress;

	private String nationality;

	private String postCode;

	private String city;

//	@NotEmpty(message = "State is required")
	private String state;

//	private String lga;
//
//	private String latitude;
//
//	private String longitude;

//	@NotEmpty(message = "Country is required")
	private String country;

	private String secondaryPhoneNumber;

}
