package ng.optisoft.rosabon.controller;

import lombok.AllArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.constant.PaymentGatewayConstant;
import ng.optisoft.rosabon.credit.dto.response.InitializePaymentResponseCredit;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.dto.request.RegisterTransactionInDto;
import ng.optisoft.rosabon.dto.response.paystack.PaystackBankTransferVerificationDto;
import ng.optisoft.rosabon.dto.response.paystack.PaystackInitializationRequest;
import ng.optisoft.rosabon.exception.NotFoundException;
import ng.optisoft.rosabon.mapper.GlobalTransactionMapper;
import ng.optisoft.rosabon.model.GlobalTransaction;
import ng.optisoft.rosabon.service.GlobalTransactnMngr;
import ng.optisoft.rosabon.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping(ApiRoute.AUTH)
public class PaymentController {

    private PaymentService paymentService;
    private GlobalTransactnMngr globalTransactnMngr;
    private UseraccountDao useraccountDao;


    @PostMapping(value =  ApiRoute.TRANSACTIONS)
    public ResponseEntity<?> registerTransaction(Authentication auth,
                                                 @RequestBody @Valid RegisterTransactionInDto dto) {
        GlobalTransaction transaction = globalTransactnMngr.create(
                useraccountDao.findByEmail(auth.getName()), dto);

        return new ResponseEntity<>(GlobalTransactionMapper.mapToDto(transaction), HttpStatus.CREATED);
    }

    @PostMapping(value =  ApiRoute.TRANSACTIONS + ApiRoute.SEPARATOR + ApiRoute.CREDIT)
    public ResponseEntity<?> registerTransactionCredit(Authentication auth,
                                                 @RequestBody @Valid RegisterTransactionInDto dto) {
        GlobalTransaction transaction = globalTransactnMngr.create(
                useraccountDao.findByEmail(auth.getName()), dto);

        return new ResponseEntity<>(GlobalTransactionMapper.mapToDto(transaction), HttpStatus.CREATED);
    }

    @GetMapping(value = ApiRoute.PAYMENT_VERIFICATION + "/{paymentGateway}/{transactionRef}")
    public ResponseEntity<?> verifyPaymentTransaction(Authentication auth,
                                           @PathVariable PaymentGatewayConstant paymentGateway,
                                           @PathVariable String transactionRef) {

        return ResponseEntity.ok(paymentService.verifyPayment(paymentGateway, transactionRef));

//        return ResponseEntity.ok(new GeneralResponse(HttpStatus.OK.value(), "Payment validated"));
    }



    @PostMapping(value =  ApiRoute.INITIALIZE_PAYMENT)
    public ResponseEntity<?> initializePayment(Authentication auth,
                                                 @RequestBody @Valid PaystackInitializationRequest request) {
       var  userAccount  = Optional.of(useraccountDao.findByEmail(auth.getName()));
       if(userAccount == null) throw new NotFoundException("User not found");
       String paymentUrl = paymentService.initializePayment(userAccount.get(), request, false);

        return new ResponseEntity<>(paymentUrl, HttpStatus.OK);
    }

    @PostMapping(value =  ApiRoute.INITIALIZE_LOAN_REPAYMENT)
    public ResponseEntity<?> initializeLoanRepayment(Authentication auth,
                                               @RequestBody @Valid PaystackInitializationRequest request) {
        var userAccount = Optional.of(useraccountDao.findByEmail(auth.getName()));
        if (userAccount == null) throw new NotFoundException("User not found");
        InitializePaymentResponseCredit responseCredit = paymentService.initializePaymentCredit(userAccount.get(), request, true);

        return new ResponseEntity<>(responseCredit, HttpStatus.OK);
    }

//    @PostMapping(value = ApiRoute.FUND_BANK_ACCOUNT + "/{transactionRef}")
//    public ResponseEntity<?> fundBankAccount(Authentication auth,
//                                             @PathVariable String transactionRef) {
//
//        String response = globalTransactnMngr.fundBankAccount(transactionRef);
//
//        return ResponseEntity.ok(new GeneralResponse(HttpStatus.OK.value(), response));
//    }



    @GetMapping(value = ApiRoute.BANK_TRANSFER_VERIFICATION + "/{transactionRef}")
    public ResponseEntity<?> verifyBankTransfer(Authentication auth,
                                                  @PathVariable String transactionRef) {

        PaystackBankTransferVerificationDto resp = paymentService.verifyBankTransfer(transactionRef);

        return ResponseEntity.ok(resp);
    }


}
