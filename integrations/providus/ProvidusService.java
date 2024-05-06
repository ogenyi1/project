package ng.optisoft.rosabon.integrations.providus;


import ng.optisoft.rosabon.dto.HttpResponseDto;
import ng.optisoft.rosabon.integrations.providus.dto.ProvidusResponseDTO;
import ng.optisoft.rosabon.integrations.providus.dto.ProvidusVerifyTransactionResponseDto;
import ng.optisoft.rosabon.model.UserVirtualAccount;
import ng.optisoft.rosabon.model.Useraccount;
import org.springframework.security.core.Authentication;

public interface ProvidusService {

    public ProvidusResponseDTO updateAccountName(String accountName, String accountNumber);

    public UserVirtualAccount createDynamicAccount(Authentication authentication, String accountName);

    public UserVirtualAccount getVirtualAccount(Useraccount user);

    public HttpResponseDto verifyTransaction(String sessionId);

    public HttpResponseDto blacklistAccount(String accountNumber, int flag);

    public UserVirtualAccount createReservedAccountNumber(Useraccount users, String bvn);

    public HttpResponseDto verifyTransactionBySettlementId(String settlementId);

    public HttpResponseDto verifyTransactionBySessionId(String settlementId);

    ProvidusResponseDTO settlementNotification(String XAuthSignature, ProvidusVerifyTransactionResponseDto requestDto, String settlementId);

    public HttpResponseDto repushSettlementNotification(String settlementId, String sessionId);

	UserVirtualAccount createDynamicAccount(Useraccount user, String accountName);
}
