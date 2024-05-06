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
public class ProvidusVerifyTransactionResponseDto {
  private String sessionId ;
//  @NotNull(message="SettlementId cannot be blank")
  private String settlementId;
  private String initiationTranRef ;
//  @NotNull(message="Account Number cannot be blank")
  private String accountNumber ;
  private String tranRemarks ;
//  @NotNull(message="Transaction Amount cannot be blank")
  private double transactionAmount;
//  @NotNull(message="Settled Amount cannot be blank")
  private double settledAmount;
  private double feeAmount;
  private double vatAmount;
  private String currency ;
  private String channelId ;
  private String sourceAccountNumber;
  private String sourceAccountName;
  private String sourceBankName;
  private String tranDateTime ;

  @Override
  public String toString() {
    return "ProvidusVerifyTransactionResponseDto{" +
            "sessionId='" + sessionId + '\'' +
            ", settlementId='" + settlementId + '\'' +
            ", initiationTranRef='" + initiationTranRef + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            ", tranRemarks='" + tranRemarks + '\'' +
            ", transactionAmount=" + transactionAmount +
            ", settledAmount=" + settledAmount +
            ", feeAmount=" + feeAmount +
            ", vatAmount=" + vatAmount +
            ", currency='" + currency + '\'' +
            ", channelId='" + channelId + '\'' +
            ", sourceAccountNumber='" + sourceAccountNumber + '\'' +
            ", sourceAccountName='" + sourceAccountName + '\'' +
            ", sourceBankName='" + sourceBankName + '\'' +
            ", tranDateTime='" + tranDateTime + '\'' +
            '}';
  }
}




