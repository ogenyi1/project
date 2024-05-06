package ng.optisoft.rosabon.integrations.providus.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProvidusReverseTranxResp {
    private boolean requestSuccessful;
    private String responseCode;
    private String responseMessage;
    private ProvidusReverseTranxRespData data;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Data
    @ToString
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProvidusReverseTranxRespData {
        private String sessionId;
        private String initiationTranRef;
        private String accountNumber;
        private String tranRemarks;
        private double transactionAmount;
        private double settledAmount;
        private double feeAmount;
        private double vatAmount;
        private String currency;
        private String settlementId;
        private String sourceAccountNumber;
        private String sourceAccountName;
        private String sourceBankCode;
        private String sourceBankName;
        private String channelId;
        private String tranDateTime;
        private String settlementDateTime;
        private double stampDutyFlg;
        private double postFlg;
        private double notificationAcknowledgement;
    }
}
