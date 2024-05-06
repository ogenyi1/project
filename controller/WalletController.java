package ng.optisoft.rosabon.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.credit.dao.CrLoanDao;
import ng.optisoft.rosabon.credit.enums.LoanStatus;
import ng.optisoft.rosabon.credit.model.CRLoan;
import ng.optisoft.rosabon.dao.BankAccountDao;
import ng.optisoft.rosabon.dao.UserVirtualAccountDao;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.dto.GeneralResponse;
import ng.optisoft.rosabon.dto.HttpResponseDto;
import ng.optisoft.rosabon.dto.PaginatedListDto;
import ng.optisoft.rosabon.dto.request.*;
import ng.optisoft.rosabon.dto.request.paystack.TransferAuthenticationRequest;
import ng.optisoft.rosabon.dto.request.paystack.TransferStatusRequest;
import ng.optisoft.rosabon.dto.response.*;
import ng.optisoft.rosabon.dto.response.paystack.PaystackInitializationRequest;
import ng.optisoft.rosabon.exception.NotFoundException;
import ng.optisoft.rosabon.integrations.providus.ProvidusService;
import ng.optisoft.rosabon.model.*;
import ng.optisoft.rosabon.service.*;
import ng.optisoft.rosabon.treasury.annotations.LogMethodTreasury;
import ng.optisoft.rosabon.treasury.dao.TrWemaVirtualAccountDao;
import ng.optisoft.rosabon.treasury.dto.request.TrPlanInDto;
import ng.optisoft.rosabon.treasury.model.wema.vas.WemaVirtualAccount;
import ng.optisoft.rosabon.util.Helper;
import ng.optisoft.rosabon.util.ResponseHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@AllArgsConstructor
@Slf4j
public class WalletController {
    private final TrWemaVirtualAccountDao trWemaVirtualAccountDao;
    private UseraccountDao useraccountDao;
    private WalletTransactionMngr walletTransactionMngr;
    private WalletMngr walletMngr;
    private BankAccountDao bankAccountDao;
    private CrLoanDao crLoanDao;
    private PaymentService paymentService;
    private ProvidusService providusService;
    private UserVirtualAccountDao userVirtualAccountDao;
    private WalletTransactionHistoryMngr walletTransactionHistoryMngr;
    private UseraccountMngr useraccountMngr;

    @GetMapping(value = ApiRoute.AUTH + ApiRoute.WALLET_BALANCE)
    public ResponseEntity<?> getWalletBalance(Authentication auth) {

        Useraccount useraccount = useraccountDao.findByEmail(auth.getName());
        final DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.DOWN);
        WalletDto walletDto = new WalletDto(useraccount.getWallet().getId(),
                Double.parseDouble(df.format(useraccount.getWallet().getAmount())));

        return ResponseEntity.ok(walletDto);
    }

    @GetMapping(value = ApiRoute.AUTH + ApiRoute.CREDIT_WALLET_BALANCE)
    public ResponseEntity<?> getCreditWalletBalance(Authentication auth) {

        Useraccount useraccount = useraccountDao.findByEmail(auth.getName());
        final DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.DOWN);
        WalletDto walletDto = new WalletDto(useraccount.getCreditWallet().getId(),
                Double.parseDouble(df.format(useraccount.getCreditWallet().getAmount())));
        log.info("USER:  {} . CREDIT WALLET BALANCE:  {}", useraccount.getEmail(), useraccount.getCreditWallet().getAmount());

        return ResponseEntity.ok(walletDto);
    }

    @LogMethodTreasury
    //i would do this differently but no time...
    @GetMapping(value = ApiRoute.AUTH + "/get-wallet-balance-along-with-virtual-account-info")
    public ResponseEntity<?> getWalletBalanceAlongWithVirtualAccountInfo(Authentication auth) {

        Useraccount useraccount = useraccountDao.findByEmail(auth.getName());
        final DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.DOWN);
        List<VirtualAccountInfo> virtualAccountInfos = new ArrayList<>();
        UserVirtualAccount providusVirtualAcct = providusService.getVirtualAccount(useraccount);
        VirtualAccountInfo virtualAccountInfo;
        if (null != providusVirtualAcct) {
            virtualAccountInfo = VirtualAccountInfo.builder()
                    .virtualAccountName(providusVirtualAcct.getAccountName())
                    .virtualAccountNo(providusVirtualAcct.getAccountNumber())
                    .provider("Providus")
                    .build();
            virtualAccountInfos.add(virtualAccountInfo);
        }
        WemaVirtualAccount wemaVirtualAccount = trWemaVirtualAccountDao.findByUseraccount(useraccount);
        if (null != wemaVirtualAccount) {
            virtualAccountInfo = VirtualAccountInfo.builder()
                    .virtualAccountName(useraccountMngr.getUserFullName(useraccount))
                    .virtualAccountNo(wemaVirtualAccount.getAccountNumber())
                    .provider("Wema")
                    .build();
            virtualAccountInfos.add(virtualAccountInfo);
        }
        WalletDto2 walletDto = new WalletDto2(useraccount.getWallet().getId(),
                Double.parseDouble(df.format(useraccount.getWallet().getAmount())),virtualAccountInfos);

        return ResponseEntity.ok(walletDto);
    }

    @GetMapping(value = ApiRoute.AUTH + ApiRoute.WALLET_BALANCE + ApiRoute.CREDIT)
    public ResponseEntity<?> getWalletBalanceCredit(Authentication auth) {
        try {
            Useraccount useraccount = useraccountDao.findByEmail(auth.getName());
            if (!useraccount.getPlatform().name().equals(Useraccount.Usage.CREDIT.name())) return ResponseEntity.badRequest().body("User was determined not to be on the credit portal");
                Optional<BankAccount> bankAccount = bankAccountDao.findByUseraccount(useraccount);
                List<CRLoan> activeLoansWithOverduePaymentsMoreThan90Days = crLoanDao.findAllByUseraccount(useraccount).stream().filter(crLoan -> crLoan.getLoanStatus().equals(LoanStatus.OVERDUE)).collect(Collectors.toList());
            final DecimalFormat df = new DecimalFormat("0.00");
            df.setRoundingMode(RoundingMode.DOWN);
                WalletCreditDto walletCreditDto = new WalletCreditDto(useraccount.getCreditWallet().getId(), Double.parseDouble(df.format(useraccount.getCreditWallet().getAmount())), bankAccount.isPresent(), !activeLoansWithOverduePaymentsMoreThan90Days.isEmpty(), true);
                return new ResponseEntity<>(new ResponseHelper(true, "Wallet balance fetched!!!", walletCreditDto), HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(new ResponseHelper(false, "Wallet balance could not be fetched!!!", e.getMessage(), e.getStackTrace()[0].toString(),null), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping(value = ApiRoute.AUTH + ApiRoute.WALLET_TRANSACTNS)
    public ResponseEntity<?> getWalletTransactions(Authentication auth,
                                                   @RequestParam(required = false) LocalDate startDate,
                                                   @RequestParam(required = false) LocalDate endDate,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "20") int limit) {

        Useraccount useraccount = useraccountDao.findByEmail(auth.getName());
        Wallet wallet = useraccount.getWallet();

//        PaginatedListDto<WalletTransactionDto> transactns = walletTransactionMngr.getBy(wallet, page, limit);

        PaginatedListDto<WalletTransactionHistoryDto> transactns = walletTransactionHistoryMngr.getAllTransactionHistoryByWallet(wallet, startDate, endDate, page, limit);

        return ResponseEntity.ok(transactns);
    }

    @GetMapping(value = ApiRoute.AUTH + ApiRoute.WALLET_TRANSACTNS + ApiRoute.CREDIT)
    public ResponseEntity<?> getWalletTransactionsCredit(Authentication auth,
                                                         @RequestParam(required = false) LocalDate startDate,
                                                         @RequestParam(required = false) LocalDate endDate,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "20") int limit) {

        Useraccount useraccount = useraccountDao.findByEmail(auth.getName());
        if (!useraccount.getPlatform().name().equals(Useraccount.Usage.CREDIT.name())) return ResponseEntity.badRequest().body("User was determined not to be on the credit portal");
        Wallet wallet = useraccount.getCreditWallet();

//            PaginatedListDto<WalletTransactionDto> transactns = walletTransactionMngr.getBy(wallet, page, limit);

        PaginatedListDto<WalletTransactionHistoryDto> transactns = walletTransactionHistoryMngr.getAllTransactionHistoryByWallet(wallet, startDate, endDate, page, limit);

        return ResponseEntity.ok(transactns);
    }

    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.WALLET_TRANSACTNS + ApiRoute.ALL)
    public ResponseEntity<?> getAllWalletTransacitons(Authentication auth,
                                                   @RequestParam(required = false) Long useraccountId,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int limit) {

        Wallet userWallet = null;
        if (useraccountId != null && useraccountId > 0) {
            Useraccount user = useraccountDao.findById(useraccountId).orElseThrow(
                    () -> new NotFoundException("User not found"));
            userWallet = user.getWallet();
        }

        PaginatedListDto<WalletTransactionDto> transactns = walletTransactionMngr.getBy(userWallet, page, limit);
        return ResponseEntity.ok(transactns);
    }

    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.WALLET_TRANSACTNS + ApiRoute.CREDIT + ApiRoute.ALL)
    public ResponseEntity<?> getAllWalletTransacitonsCredit(Authentication auth,
                                                      @RequestParam(required = false) Long useraccountId,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int limit) {

        Wallet userWallet = null;
        if (useraccountId != null && useraccountId > 0) {
            Useraccount user = useraccountDao.findById(useraccountId).orElseThrow(
                    () -> new NotFoundException("User not found"));
            if (!user.getUsage().equals(Useraccount.Usage.CREDIT)) return ResponseEntity.badRequest().body("User was determined not to be on the credit portal");
            userWallet = user.getCreditWallet();

        }

        PaginatedListDto<WalletTransactionDto> transactns = walletTransactionMngr.getBy(userWallet, page, limit);
        return ResponseEntity.ok(transactns);
    }


    @PostMapping(value = ApiRoute.ADMIN + ApiRoute.FUND_USER_WALLET)
    public ResponseEntity<?> fundUserWallet(Authentication auth,
                                            @RequestBody @Valid FundUserWalletInDto dto) {


        Useraccount userToBeFunded = useraccountDao.findById(dto.getUseraccountId()).orElseThrow(
                () -> new NotFoundException("User not found"));

        walletMngr.creditWallet(userToBeFunded, dto.getAmount(),
                WalletTransaction.TransactionCategory.WALLET_FUNDING_BY_ROSABON_ADMIN,
                "Wallet funding by admin", null,false,false);

        return ResponseEntity.ok(new GeneralResponse(HttpStatus.OK.value(), "Wallet successfully funded"));
    }

    @PostMapping(value = ApiRoute.AUTH + ApiRoute.FUND_USER_WALLET + ApiRoute.CREDIT + ApiRoute.DEBIT_CARD)
    public ResponseEntity<?> fundUserWalletCreditByDebitCard(Authentication auth, @RequestBody @Valid FundUserWalletInDto dto) {
        try {
            Useraccount user = useraccountDao.findByEmail(auth.getName());
            if (!user.getPlatform().name().equals(Useraccount.Usage.CREDIT.name())) return ResponseEntity.badRequest().body("User was determined not to be on the credit portal");
            PaystackInitializationRequest paystackInitializationRequest = new PaystackInitializationRequest(user.getEmail(), String.valueOf(dto.getAmount()), GlobalTransaction.PurposeOfPayment.WALLET_FUNDING,null,false);
            return new ResponseEntity<>(new ResponseHelper(true, "Payment link generated!!!",paymentService.initializePayment(user, paystackInitializationRequest, true)), HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(new ResponseHelper(false, "Payment link could not generated!!!", e.getMessage(), e.getStackTrace()[0].toString(),null), HttpStatus.EXPECTATION_FAILED);
        }
    }

//    @PostMapping(value = ApiRoute.AUTH +ApiRoute.CREDIT+  ApiRoute.FUND_USER_WALLET  + ApiRoute.DEBIT_CARD)
//    public HttpResponseDto updateOnSuccessfulPaymentWithDebitCardOnCredit(Authentication auth,
//                                                                          @RequestParam String transactionReference) {
//
//        return service.updateOnSuccessfulPaymentWithDebitCard(id, transactionReference,dto);
//    }

    @GetMapping(value = ApiRoute.AUTH + ApiRoute.FUND_USER_WALLET + ApiRoute.CREDIT + ApiRoute.BANK_ACCOUNT)
    public ResponseEntity<?> fundUserWalletCreditByBankAccount(Authentication auth) {
        try {
            Useraccount user = useraccountDao.findByEmail(auth.getName());
            if (!user.getPlatform().name().equals(Useraccount.Usage.CREDIT.name())) return ResponseEntity.badRequest().body("User was determined not to be on the credit portal");
//            return ResponseEntity.ok().body(providusService.getVirtualAccount(user));
            return new ResponseEntity<>(new ResponseHelper(true, "Rosabon account retrieved!!!",providusService.getVirtualAccount(user)), HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(new ResponseHelper(false, "Error retrieving Rosabon account number!!!",  e.getMessage(), e.getStackTrace()[0].toString(),null), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping(value = ApiRoute.AUTH + ApiRoute.WALLETS + ApiRoute.WITHDRAW_TO_BANK)
    public ResponseEntity<?> withdrawFromCreditWalletToBank(Authentication auth, @RequestBody WithdrawInDto request) {
        Useraccount user = useraccountDao.findByEmail(auth.getName());
        if (!user.getUsage().equals(Useraccount.Usage.CREDIT)) return ResponseEntity.badRequest().body("User was determined not to be on the credit portal");
        return walletMngr.withdrawFromCreditWalletToBank(user, request);
    }

    @PostMapping(ApiRoute.AUTH + ApiRoute.CREDIT + ApiRoute.FUND_USER_TREASURY_WALLET)
    public ResponseEntity<?> fundUserTreasuryWallet (Authentication auth,@RequestBody @Valid FundUserWalletInDto dto) {
        try {
            Useraccount user = useraccountDao.findByEmail(auth.getName());
            if (!user.getPlatform().name().equals(Useraccount.Usage.CREDIT.name())) return ResponseEntity.badRequest().body("User was determined not to be on the credit portal");
            walletMngr.debitCreditWallet(user, dto.getAmount(), WalletTransaction.TransactionCategory.WALLET_FUNDING_BY_CREDIT_WALLET,WalletTransaction.TransactionCategory.WALLET_FUNDING_BY_CREDIT_WALLET.toString(), null);
            walletMngr.creditWallet(user, dto.getAmount(), WalletTransaction.TransactionCategory.WALLET_FUNDING_BY_CREDIT_WALLET,WalletTransaction.TransactionCategory.WALLET_FUNDING_BY_CREDIT_WALLET.toString(), null,false,false);
            Wallet updatedCreditWallet = useraccountDao.findById(dto.getUseraccountId()).get().getCreditWallet();
            WalletDto updatedCreditWalletDto = new WalletDto(updatedCreditWallet.getId(),updatedCreditWallet.getAmount());
            return new ResponseEntity<>(new ResponseHelper(true, "Treasury wallet credited!!!",updatedCreditWalletDto), HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(new ResponseHelper(false, "Error processing transaction!!!",  e.getMessage(), e.getStackTrace()[0].toString(),null), HttpStatus.EXPECTATION_FAILED);
        }
    }
    
    @PostMapping(value = ApiRoute.WEB_HOOK + ApiRoute.GET_TRANSFER_STATUS)
    public ResponseEntity<?> checkForTransferStatus(@RequestBody TransferStatusRequest request)
    {
    	log.info(" --- WebHook called --- ");
    	
    	walletMngr.checkForTransferStatus(request);
    	
    	return ResponseEntity.ok("Transfer status checked!");
    }

    @PostMapping(value = ApiRoute.AUTH_TRANSFER)
    public ResponseEntity<?> authenticateTransfer(@RequestBody TransferAuthenticationRequest request)
    {
//    	log.info(" --- WebHook called --- ");
    	
    	return ResponseEntity.ok(walletMngr.authenticateTransfer(request));
    }
    
    @PostMapping(value = ApiRoute.AUTH + ApiRoute.WALLETS + ApiRoute.REQUEST_WITHDRAWAL)
    public ResponseEntity<?> requestWithdrawal(Authentication auth, @RequestBody @Valid @NotNull WithdrawInDto request) {
    	Useraccount user = useraccountDao.findByEmail(auth.getName());
    	return ResponseEntity.status(HttpStatus.CREATED).body(walletMngr.requestWithdrawal(user, request));
    }
    
    @PostMapping(value = ApiRoute.AUTH + ApiRoute.WALLETS + ApiRoute.WALLET_TRANSFER)
    public ResponseEntity<?> transferToPlan(Authentication auth, @RequestBody @Valid @NotNull TransferToPlanRequest request)
    {    	
    	Useraccount user = useraccountDao.findByEmail(auth.getName());
    	
    	return ResponseEntity.status(HttpStatus.CREATED).body(walletMngr.transferToPlan(user, request));
    }
    
    @GetMapping(value = ApiRoute.AUTH + ApiRoute.WALLET_TRANSFER + ApiRoute.ELIGIBLE_PLANS_FOR_TRANSFER)
    public ResponseEntity<?> getEligiblePlansForTransfer(Authentication auth,
                                                         @RequestParam(value = "sort", defaultValue = "createdDate") String sort )
    {
    	Useraccount user = useraccountDao.findByEmail(auth.getName());
    	
    	return ResponseEntity.ok(walletMngr.getEligiblePlansForTransfer(user, sort));
    }
    
    @PutMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.WALLETS + ApiRoute.UPDATE_AFTER_REQUEST_APPROVAL)
    public ResponseEntity<?> updateAfterWithdrawalRequestApproval(Authentication auth, @PathVariable Long withdrawalRequestId)
    {
//    	log.info(" --- WebHook called --- ");
    	
//    	Useraccount user = useraccountDao.findByEmail(auth.getName());
    	
    	return ResponseEntity.ok(walletMngr.updateAfterWithdrawalRequestApproval(withdrawalRequestId));
    }
    
    @PostMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.WALLETS + ApiRoute.ACCEPT_OR_REJECT_WITHDRAWAL_REQUEST)
    public ResponseEntity<?> acceptOrRejectWithdrawalRequest(Authentication auth, AcceptOrRejectWithdrawalRequest request)
    {
//    	log.info(" --- WebHook called --- ");
    	
//    	Useraccount user = useraccountDao.findByEmail(auth.getName());
    	
    	return ResponseEntity.ok(walletMngr.acceptOrRejectWithdrawalRequest(request));
    }
    
    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.WALLETS + ApiRoute.GET_TOTAL_WALLET_BALANCE)
    public ResponseEntity<?> getTotalWalletBalance() {
        return ResponseEntity.ok(walletMngr.getTotalWalletBalance());
    }

    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.WALLETS + ApiRoute.CREDIT + ApiRoute.GET_TOTAL_WALLET_BALANCE)
    public ResponseEntity<?> getTotalWalletBalanceCredit() {
        return ResponseEntity.ok(walletMngr.getTotalWalletBalanceCredit());
    }

    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.WALLETS + ApiRoute.GET_CUSTOMER_WALLET_BALANCE + "/{customer-id}")
    public ResponseEntity<?> getCustomerWalletBalance(@PathVariable("customer-id") String customerId) {
        return ResponseEntity.ok(walletMngr.getCustomerWalletBalance(customerId));
    }

    @PreAuthorize("hasAnyAuthority('VIEW_WALLET_MANAGEMENT', 'SUPER_ADMINISTRATOR')")
    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.WALLETS + ApiRoute.CREDIT + ApiRoute.GET_CUSTOMER_WALLET_BALANCE + "/{customer-id}")
    public ResponseEntity<?> getCustomerWalletBalanceCredit(@PathVariable("customer-id") Long id) {
        return ResponseEntity.ok(walletMngr.getCustomerWalletBalanceCredit(id));
    }

    @PostMapping(value = ApiRoute.ADMIN + ApiRoute.WALLETS + ApiRoute.SET_ACTIVATION_REFERRAL_BONUS)
    public ResponseEntity<?> setActivationReferralBonus(@RequestParam Double amount)
    {
    	return ResponseEntity.ok(walletMngr.setActivationReferralBonus(amount));
    }

    @PostMapping(value = ApiRoute.ADMIN + ApiRoute.WALLETS + ApiRoute.CREDIT + ApiRoute.SET_ACTIVATION_REFERRAL_BONUS )
    public ResponseEntity<?> setActivationReferralBonusCredit(@RequestParam Double amount)
    {
        return ResponseEntity.ok(walletMngr.setActivationReferralBonusCredit(amount));
    }
    
    @PreAuthorize("hasAnyAuthority('VIEW_WALLET_MANAGEMENT', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value = ApiRoute.ADMIN + ApiRoute.WALLETS + ApiRoute.SET_REFERRAL_REDEEM_THRESHOLD)
    public ResponseEntity<?> setReferralRedeemThreshold(@RequestParam Double amount)
    {
    	return ResponseEntity.ok(walletMngr.setReferralRedeemThreshold(amount));
    }
    
    @GetMapping(value = ApiRoute.AUTH + ApiRoute.WALLETS + ApiRoute.GET_REFERRAL_REDEEM_THRESHOLD)
    public ResponseEntity<?> getReferralRedeemThreshold()
    {
    	return ResponseEntity.ok(walletMngr.getReferralRedeemThreshold());
    }
    
    @GetMapping(value = ApiRoute.AUTH + ApiRoute.WALLETS + ApiRoute.GET_ACTIVATION_REFERRAL_BONUS)
    public ResponseEntity<?> getActivationReferralBonus()
    {
    	return ResponseEntity.ok(walletMngr.getActivationReferralBonus());
    }
    
    @GetMapping(value = ApiRoute.AUTH + ApiRoute.WALLETS + ApiRoute.GET_ACTIVATION_REFERRAL_BONUS + ApiRoute.ACTIVITIES)
    public ResponseEntity<?> getActivationReferralBonusActivity(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int limit){
    	return ResponseEntity.ok(walletMngr.getActivationReferralBonusActivity(page, limit));
    }
    
    @GetMapping(value = ApiRoute.AUTH + ApiRoute.WALLETS + ApiRoute.GET_REFERRAL_REDEEM_THRESHOLD + ApiRoute.ACTIVITIES)
    public ResponseEntity<?> getReferralRedeemThresholdActivity(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int limit){
    	return ResponseEntity.ok(walletMngr.getReferralRedeemThresholdActivity(page, limit));
    }
    
    @GetMapping(value = ApiRoute.AUTH + ApiRoute.WALLETS + ApiRoute.GET_REACTIVATION_REFERRAL_BONUS + ApiRoute.ACTIVITIES)
    public ResponseEntity<?> getReactivationReferralBonusActivity(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int limit){
    	return ResponseEntity.ok(walletMngr.getReactivationReferralBonusActivity(page, limit));
    }
    
    @GetMapping(value = ApiRoute.AUTH + ApiRoute.WALLETS + ApiRoute.GET_REACTIVATION_REFERRAL_BONUS)
    public ResponseEntity<?> getReactivationReferralBonus()
    {
    	return ResponseEntity.ok(walletMngr.getReactivationReferralBonus());
    }
    
    @PreAuthorize("hasAnyAuthority('VIEW_WALLET_MANAGEMENT', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value = ApiRoute.ADMIN + ApiRoute.WALLETS + ApiRoute.SET_REACTIVATION_REFERRAL_BONUS)
    public ResponseEntity<?> setReactivationReferralBonus(@RequestBody ReactivationReferralBonusRequest request)
    {
    	return ResponseEntity.ok(walletMngr.setReactivationReferralBonus(request));
    }
}
