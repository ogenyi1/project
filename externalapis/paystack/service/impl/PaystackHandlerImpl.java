package ng.optisoft.rosabon.externalapis.paystack.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ng.optisoft.rosabon.dto.request.paystack.AccountVerificationRequest;
import ng.optisoft.rosabon.dto.request.paystack.InitiateTransferRequest;
import ng.optisoft.rosabon.dto.request.paystack.PaystackTransferRecipientRequest;
import ng.optisoft.rosabon.dto.request.paystack.TransferAuthenticationRequest;
import ng.optisoft.rosabon.dto.response.paystack.InitiateTransferResponse;
import ng.optisoft.rosabon.dto.response.paystack.PaystackAccountVerificationResponseDto;
import ng.optisoft.rosabon.dto.response.paystack.PaystackTransferRecipientResponse;
import ng.optisoft.rosabon.dto.response.paystack.TransferAuthenticationViaOtp;
import ng.optisoft.rosabon.exception.BadRequestException;
import ng.optisoft.rosabon.credit.externalapis.paystack.pojo.VerifyTransaction;
import ng.optisoft.rosabon.credit.externalapis.paystack.service.PaystackHandler;
import ng.optisoft.rosabon.generators.PaystackServiceGenerator;
import ng.optisoft.rosabon.proxies.PaystackServiceProxy;
import retrofit2.Call;
import retrofit2.Response;

@Service
public class PaystackHandlerImpl implements PaystackHandler
{
	@Value("${paystack.test.key}")
	private String token;

	@Override
    public VerifyTransaction verifyTransaction(String ref, String token) {

        PaystackServiceProxy service = PaystackServiceGenerator.createService(PaystackServiceProxy.class, token);

        Call<VerifyTransaction> callAsync = service.verifyUserTransaction(ref);

        try {
            Response<VerifyTransaction> response = callAsync.execute();

            return response.body();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
    
	@Override
    public PaystackAccountVerificationResponseDto verifyAccountNumber(AccountVerificationRequest request) {
 
        PaystackServiceProxy service = PaystackServiceGenerator.createService(PaystackServiceProxy.class, token);

        Call<PaystackAccountVerificationResponseDto> callSync = service.verifyAccountNumber(request.getAccountNumber(), request.getBankCode());

        try {
            Response<PaystackAccountVerificationResponseDto> response = callSync.execute();

            return response.body();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BadRequestException(ex.getMessage());
        }

//        return null;
    }
    
	@Override
    public PaystackTransferRecipientResponse createTransferRecipient(PaystackTransferRecipientRequest request) {
    	 
        PaystackServiceProxy service = PaystackServiceGenerator.createService(PaystackServiceProxy.class, token);

        Call<PaystackTransferRecipientResponse> callSync = service.createTransferRecipient(request);

        try {
            Response<PaystackTransferRecipientResponse> response = callSync.execute();

            return response.body();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BadRequestException(ex.getMessage());
        }

//        return null;
    }
	
	@Override
    public InitiateTransferResponse initiateTransfer(InitiateTransferRequest request) {
    	 
        PaystackServiceProxy service = PaystackServiceGenerator.createService(PaystackServiceProxy.class, token);

        Call<InitiateTransferResponse> callSync = service.initiateTransfer(request);

        try {
            Response<InitiateTransferResponse> response = callSync.execute();

            return response.body();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BadRequestException(ex.getMessage());
        }

//        return null;
    }
	
	@Override
    public TransferAuthenticationViaOtp authenticateTransferViaOtp(TransferAuthenticationRequest request) {
    	 
        PaystackServiceProxy service = PaystackServiceGenerator.createService(PaystackServiceProxy.class, token);

        Call<TransferAuthenticationViaOtp> callSync = service.authenticateTransfer(request);

        try {
            Response<TransferAuthenticationViaOtp> response = callSync.execute();

            return response.body();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BadRequestException(ex.getMessage());
        }

//        return null;
    }
}
