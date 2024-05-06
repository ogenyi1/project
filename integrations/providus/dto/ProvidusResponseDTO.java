package ng.optisoft.rosabon.integrations.providus.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import ng.optisoft.rosabon.util.ToStringUtil;

/**
 * @author Elesho John
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class ProvidusResponseDTO {

    private String sessionId;
//    private String account_name;
//    private  String account_number;
//    private String initiationTranRef;
    private boolean requestSuccessful;
//    @Enum(message = "Providus Response Message", enumClass = ProvidusResponseMessage.class)
    private String responseMessage;
//    @Enum(message = "Providus Response Code", enumClass = ProvidusResponseCode.class)
    private String responseCode;

    public ProvidusResponseDTO(String sessionId, boolean b, String message, String code) {
        this.sessionId = sessionId;
        this.requestSuccessful = b;
        this.responseMessage = message;
        this.responseCode = code;
    }

    @Override
    public String toString() {
        return ToStringUtil.print(this);
    }
}
