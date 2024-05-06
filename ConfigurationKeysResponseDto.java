package ng.optisoft.rosabon.dto;

import lombok.Data;

@Data
public class ConfigurationKeysResponseDto {

	private String paystackPublicKey;

	private String paystackSecretKey;

	private String ravePublicKey;

	private String raveSecretKey;

	private String monnifyPublicKey;

	private String monnifySecretKey;

	private String monnifyContractCode;

	private String providusClientId;

	private String providusClientSecret;

	private String providusXAuthSignature;

}
