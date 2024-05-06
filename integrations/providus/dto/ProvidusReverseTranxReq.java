package ng.optisoft.rosabon.integrations.providus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProvidusReverseTranxReq {
    @JsonProperty("settlement_id")
    private String settlementId;
    @JsonProperty("session_id")
    private String sessionId;

}
