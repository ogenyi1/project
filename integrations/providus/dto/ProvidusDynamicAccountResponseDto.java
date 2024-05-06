package ng.optisoft.rosabon.integrations.providus.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Elesho John
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvidusDynamicAccountResponseDto {

    private String account_number;
    private String account_name;
    private Boolean requestSuccessful;
    private String responseMessage;
    private String responseCode;
    private String initiationTranRef;
}
