package ng.optisoft.rosabon.integrations.providus;


import io.jsonwebtoken.lang.Strings;
import lombok.extern.slf4j.Slf4j;
import ng.optisoft.rosabon.constant.APIMessage;
import ng.optisoft.rosabon.dao.*;
import ng.optisoft.rosabon.dto.EmailTemplateDto;
import ng.optisoft.rosabon.dto.HttpResponseDto;
import ng.optisoft.rosabon.dto.response.CreateAuditTrailDto;
import ng.optisoft.rosabon.enums.*;
import ng.optisoft.rosabon.event.AuditTrailEvent;
import ng.optisoft.rosabon.event.EmailEvent;
import ng.optisoft.rosabon.event.InAppEvent;
import ng.optisoft.rosabon.exception.BadRequestException;
import ng.optisoft.rosabon.exception.NotFoundException;
import ng.optisoft.rosabon.integrations.providus.dto.*;
import ng.optisoft.rosabon.model.*;
import ng.optisoft.rosabon.service.*;
import ng.optisoft.rosabon.treasury.model.TrPlan;
import ng.optisoft.rosabon.treasury.service.TrPlanActionService;
import ng.optisoft.rosabon.treasury.service.TrPlanService;
import ng.optisoft.rosabon.util.Helper;
import org.cloudinary.json.JSONException;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;


/**
 * @author Elesho John
 */

@Slf4j
@Service
public class ProvidusServiceImpl implements ProvidusService {
    @Autowired
    private UserVirtualAccountReservedDao userVirtualAccountReservedDao;

    String BASE_URL;
    String URL;
    String isLive;

//    @Autowired
//    private TransactionLogServiceContract transactionLogService;
//

//    @Autowired
//    private UserWalletContract userWalletService;

    //    @Value("${providus.base-url}")
//   private  String BASE_URL;
//    @Value("${providus.url}")
//   private  String URL;
//   @Value("${providus.client-id}")
//   private  String CLIENTID;
//    @Value("${providus.client-secret}")
//    private  String CLIENTSECRET;
//    @Value("${providus.x-auth-signature}")
//    private  String X_AUTH_SIGNATURE;
//
    String CLIENTID;
    String CLIENTSECRET;
    String X_AUTH_SIGNATURE;
    long startTime;
    @Autowired
    TrPlanService planService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UseraccountMngr userService;
    @Autowired
    private UseraccountDao userDao;
    //
//    @Autowired
//    private ConfigurationServiceContract configurationService;
//
    @Autowired
    private NotificationMngr notify;
    @Autowired
    private UserVirtualAccountService userVirtualAccountService;
    @Autowired
    private ConfigurationServiceContract configurationService;
    @Autowired
    private TrPlanActionService planActionContract;

    @Autowired
    private WalletMngr walletMngr;

    @Autowired
    private UserVirtualAccountDao virtualAccountDao;

    @Autowired
    private ProvidusReversalDao providusReversalDao;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private EmailService emailService;

    @Autowired
    private WalletTransactionHistoryDao walletTransactionHistoryDao;

//    @Value("${spring.profiles.active:}")
//    public void setActiveProfile(String activeProfileValue) {
//        activeProfile = activeProfileValue;

    @Override
    public ProvidusResponseDTO updateAccountName(String accountName, String accountNumber) {

        JSONObject body = new JSONObject();
        try {
            body.put("account_name", accountName);
            body.put("account_number", accountNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        log.info(String.valueOf(body));
//        URL = BASE_URL + "/PiPUpdateAccountName";
//        log.info(URL);

        URL = BASE_URL + configurationService
                .getByKey(ConfigKeyType.PROVIDUS_UPDATE_ACCOUNT_NAME_URL).getConfigValue();
        log.info(URL);
        HttpEntity<String> request = new HttpEntity(body.toString(), Helper.getProvidusHeaders(CLIENTID, X_AUTH_SIGNATURE));
        log.info(request.toString());
        ResponseEntity<ProvidusResponseDTO> response = restTemplate
                .exchange(URL, HttpMethod.POST, request,
                        ProvidusResponseDTO.class);
        if (Helper.isNotEmpty(response.getBody()) && response.getBody().isRequestSuccessful()) {
            log.info("Update Account Name went successful");


            return response.getBody();
        }

        throw new BadRequestException(
                response.toString());

    }

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    @Override
    public UserVirtualAccount createDynamicAccount(Authentication auth, String accountName) {

        throw new BadRequestException("This payment method is not available. Please use other payment methods");

//        Useraccount user = userDao.findByEmail(auth.getName());
//
//        UserVirtualAccount virtualAccount = createDynamicAccount(user, accountName);
//
//        return virtualAccount;
    }

    private void init() {

        startTime = System.currentTimeMillis();
        isLive = configurationService.getByKey(ConfigKeyType.PROVIDUS_IS_LIVE)
                .getConfigValue();

        if (Boolean.valueOf(isLive).equals(Boolean.TRUE)) {
            BASE_URL = configurationService.getByKey(ConfigKeyType.PROVIDUS_LIVE_BASE_URL)
                    .getConfigValue();
            CLIENTID = configurationService
                    .getByKey(ConfigKeyType.PROVIDUS_LIVE_CLIENT_ID).getConfigValue();
            CLIENTSECRET = configurationService
                    .getByKey(ConfigKeyType.PROVIDUS_LIVE_CLIENT_SECRET).getConfigValue();
            //     X_AUTH_SIGNATURE =  sha512(CLIENTID +':' + CLIENTSECRET).toString();

            X_AUTH_SIGNATURE = configurationService
                    .getByKey(ConfigKeyType.PROVIDUS_LIVE_X_AUTH_SIGNATURE).getConfigValue();
        } else {
            BASE_URL = configurationService.getByKey(ConfigKeyType.PROVIDUS_TEST_BASE_URL)
                    .getConfigValue();
            CLIENTID = configurationService
                    .getByKey(ConfigKeyType.PROVIDUS_TEST_CLIENT_ID).getConfigValue();
            CLIENTSECRET = configurationService
                    .getByKey(ConfigKeyType.PROVIDUS_TEST_CLIENT_SECRET).getConfigValue();
            X_AUTH_SIGNATURE = configurationService
                    .getByKey(ConfigKeyType.PROVIDUS_TEST_X_AUTH_SIGNATURE).getConfigValue();
        }
    }

    @Override
    public UserVirtualAccount getVirtualAccount(Useraccount user) {

        if (Helper.isEmpty(user))
            throw new NotFoundException("User does not exist.");

        UserVirtualAccount virtualAccount = userVirtualAccountService.findByUser(user);
        if (Helper.isEmpty(virtualAccount)) {
            log.warn(
                    "User does not have a reserved virtual account Number");

//            log.info(
//                    "Generating virtual account for user");

//            virtualAccount = createReservedAccountNumber(user, "");

        }
        return virtualAccount;
    }

    @Override
    public HttpResponseDto verifyTransaction(String sessionId) {

        if (Helper.isEmpty(sessionId)) {
            log.warn(
                    "Session Id is required");
            throw new BadRequestException(
                    "Session Id is required");
        }

        log.info("Verify Transaction by Session Id: {}", sessionId);
        URL = BASE_URL + configurationService
                .getByKey(ConfigKeyType.PROVIDUS_VERIFY_TRANSACTION_URL).getConfigValue() + sessionId;
        log.info(URL);
//        URL = BASE_URL + "/PiPverifyTransaction?session_id=" + sessionId;
//        log.info(URL);
        HttpEntity<String> request = new HttpEntity(Helper.getProvidusHeaders(CLIENTID, X_AUTH_SIGNATURE));
        log.info(request.toString());
        ResponseEntity<ProvidusVerifyTransactionResponseDto> response = restTemplate
                .exchange(URL, HttpMethod.GET, request,
                        ProvidusVerifyTransactionResponseDto.class);
        if (Helper.isNotEmpty(response.getBody())) {
            log.info("Verify Transaction went successful");

            return new HttpResponseDto(APIMessage.SUCCESSFUL_REQUEST.concat(
                    "Transaction has been verified."),
                    HttpStatus.OK, "Update", response);
        }
        throw new BadRequestException(
                request.toString());

    }

    @Override
    public HttpResponseDto blacklistAccount(String accountNumber, int flag) {
        if (Helper.isEmpty(accountNumber)) {
            log.warn(
                    "Account Number is required");
            throw new BadRequestException(
                    "Account Number Id is required");
        }

        JSONObject body = new JSONObject();
        try {
            body.put("blacklist_flg", flag);
            body.put("account_number", accountNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        log.info(String.valueOf(body));
        log.info("Blacklist Account: {}", accountNumber);
        URL = BASE_URL + configurationService
                .getByKey(ConfigKeyType.PROVIDUS_BLACKLIST_ACCOUNT_URL).getConfigValue();
        log.info(URL);
//        URL = BASE_URL + "/PiPBlacklistAccount";
//        log.info(URL);
        HttpEntity<String> request = new HttpEntity(body.toString(), Helper.getProvidusHeaders(CLIENTID, X_AUTH_SIGNATURE));
        log.info(request.toString());
        ResponseEntity<ProvidusResponseDTO> response = restTemplate
                .exchange(URL, HttpMethod.GET, request,
                        ProvidusResponseDTO.class);
        if (Helper.isNotEmpty(response.getBody())) {
            log.info("BlackList went successful");


            return new HttpResponseDto(APIMessage.SUCCESSFUL_REQUEST.concat(
                    "Account Blacklisted."),
                    HttpStatus.OK, "Verify", response);
        }
        throw new BadRequestException(
                request.toString());

    }

    @Override
    public UserVirtualAccount createReservedAccountNumber(Useraccount user, String bvn) {
        init();

        if (Helper.isEmpty(user))
            throw new NotFoundException("User does not exist.");

        String accountName = StringUtils.capitalize(userService.getUserFullName(user));

        JSONObject body = new JSONObject();
        JSONObject body2 = new JSONObject();

        log.info("fetching unused reserved accounts from database");

        Optional<UserVirtualAccountReserved> reservedVirtualAccountOpt = userVirtualAccountReservedDao
                .findFirstByStatus(EntityStatus.UNASSIGNED);

        UserVirtualAccountReserved userVirtualAccountReserved;

        if (reservedVirtualAccountOpt.isPresent()) {
            log.info("unassigned reserved account is available");
            userVirtualAccountReserved = reservedVirtualAccountOpt.get();
            userVirtualAccountReserved.setUserAccountId(user.getId());
            userVirtualAccountReserved.setStatus(EntityStatus.ASSIGNED);
            userVirtualAccountReserved.setCreatedAt2(LocalDateTime.now());
            body2.put("account_number", userVirtualAccountReserved.getAccountNumber());
            body2.put("account_name", accountName);

            log.info("attempting to update account name of reserved account before reassigning");

            log.info("Request body is : {}", body2);

            log.info("Reassigning Reserved Account with current name {} to new name of : {}",
                    userVirtualAccountReserved.getAccountName(),
                    accountName);

            URL = BASE_URL + configurationService
                    .getByKey(ConfigKeyType.PROVIDUS_UPDATE_ACCOUNT_NAME_URL).getConfigValue();

            log.info(URL);

            HttpEntity<String> request = new HttpEntity<>(body2.toString(), Helper.getProvidusHeaders(CLIENTID, X_AUTH_SIGNATURE));

//            log.info("request is : {}", request);

            ResponseEntity<ProvidusDynamicAccountResponseDto> response = restTemplate
                    .exchange(URL, HttpMethod.POST, request,
                            ProvidusDynamicAccountResponseDto.class);

            log.info("Response obtained from providus when trying to update reserved virtual account name is : " + response);

//            ProvidusDynamicAccountResponseDto providusDynamicAccountResponseDto = new ProvidusDynamicAccountResponseDto();
            ProvidusDynamicAccountResponseDto providusDynamicAccountResponseDto = new ProvidusDynamicAccountResponseDto(
                    userVirtualAccountReserved.getAccountNumber(),
                    accountName,
                    true,
                    "OPERATION SUCCESSFUL",
                    "00",
                    "");

            if (Helper.isNotEmpty(providusDynamicAccountResponseDto)
                    && providusDynamicAccountResponseDto.getRequestSuccessful().equals(Boolean.TRUE)
                    && Helper.isNotEmpty(providusDynamicAccountResponseDto.getResponseCode())
                    && providusDynamicAccountResponseDto.getResponseCode().equals("00")) {
                log.info("request was successful");
                log.info("proceeding to add new row to virtual accounts table");
                providusDynamicAccountResponseDto.setAccount_number( userVirtualAccountReserved.getAccountNumber());
                providusDynamicAccountResponseDto.setAccount_name("ROSABON(" + accountName + ")");
                log.info("updating unassigned account status to ASSIGNED");
                UserVirtualAccount uva = virtualAccountDao.findByAccountNumber(userVirtualAccountReserved.getAccountNumber());
                if (uva != null) {
                    log.info("account number already existed on virtual account table");
                    log.info("former user : {}",uva.getUserAccount().getId());
                    userVirtualAccountReserved.setUserAccountId(uva.getUserAccount().getId());
                    userVirtualAccountReserved.setStatus(EntityStatus.ASSIGNED);
                    userVirtualAccountReservedDao.save(userVirtualAccountReserved);
                    return uva;
                }
                userVirtualAccountReservedDao.save(userVirtualAccountReserved);
                log.info("updated unassigned account");
                return userVirtualAccountService.create(providusDynamicAccountResponseDto, user, UserVirtualAccount.VirtualAccountType.RESERVED);
            }
            log.info("PiPUpdateAccountName call was not successful for this account so we are not updating");


        }else {
            log.info("out of unassigned accounts...");
            log.info("attempting to generate reserved account using the api...");

            try {
                body.put("bvn", bvn);
                body.put("account_name", accountName);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            log.info("Request body is : {}", body);
            log.info("Creating Reserved Account with name : {}", accountName);

            URL = BASE_URL + configurationService
                    .getByKey(ConfigKeyType.PROVIDUS_CREATE_RESERVED_ACCOUNT_NUMBER_URL).getConfigValue();

            log.info(URL);
//        URL = BASE_URL + "/PiPCreateReservedAccountNumber";
//        log.info(URL);
            HttpEntity<String> request = new HttpEntity(body.toString(), Helper.getProvidusHeaders(CLIENTID, X_AUTH_SIGNATURE));

//            log.info(request.toString());

            ResponseEntity<ProvidusDynamicAccountResponseDto> response = restTemplate
                    .exchange(URL, HttpMethod.POST, request,
                            ProvidusDynamicAccountResponseDto.class);

            log.info("Response obtained from providus when trying to generate reserved virtual account : " + response);

            if (Helper.isNotEmpty(response)
                    &&Helper.isNotEmpty(response.getBody())
                    && response.getBody().getRequestSuccessful().equals(Boolean.TRUE)
                    && Helper.isNotEmpty(response.getBody().getAccount_number())) {

                checkForDuplicateVirtualAccountNumber(response);
                log.info("Create Reserved Account Number went successful");
//                log.info("Update the account name removing Merchant()");
//            ProvidusResponseDTO isupdateSuccessful = updateAccountName(user.getFullName(), response.getBody().getAccount_number());
//
//            if (isupdateSuccessful.isRequestSuccessful()) {
//                response.getBody().setAccount_name(user.getFullName());
//                log.info("Confirming Update {}", response.getBody().toString());
//
//            }
                return userVirtualAccountService.create(response.getBody(), user, UserVirtualAccount.VirtualAccountType.RESERVED);
            }

        }
        log.info("both methods failed");
        log.info("throwing exception");
        throw new BadRequestException("Could not Generate Virtual Account ");

    }

//    @EventListener(ApplicationStartedEvent.class)
    public void changeNameOnReassignedVirtualAccounts () {
        init();
        if (activeProfile.equals("prod")) {
            log.info("changing name on reassigned virtual account");
            List<UserVirtualAccountReserved> assignedAccounts = userVirtualAccountReservedDao.findAllByStatus(EntityStatus.ASSIGNED);
            assignedAccounts.forEach(assignedAccount -> {
                UserVirtualAccount virtualAccount;
                try {
                    log.info("virtual account number in question : {}", assignedAccount.getAccountNumber());
                    virtualAccount = virtualAccountDao.findByAccountNumber(assignedAccount.getAccountNumber());
                    if (virtualAccount == null) {
                        log.info("virtual account is null... No virtual account present for this assigned account");
                        log.info("creating reserved virtual account");
                        Useraccount user = userDao.findById(assignedAccount.getUserAccountId()).orElseThrow();
                        ProvidusDynamicAccountResponseDto providusDynamicAccountResponseDto = new ProvidusDynamicAccountResponseDto(
                                assignedAccount.getAccountNumber(),
                                Strings.capitalize(userService.getUserFullName(user)),
                                true,
                                "OPERATION SUCCESSFUL",
                                "00",
                                "");
                        virtualAccount = userVirtualAccountService.create(providusDynamicAccountResponseDto, user, UserVirtualAccount.VirtualAccountType.RESERVED);
                    }
                }catch (Exception e) {
                    log.info("exception encountered was :\n {}",e.getMessage());
                    return;
                }
                log.info("associated virtual account id is : {}", virtualAccount.getId());
                log.info("current virtual account name is : {}", virtualAccount.getAccountName());
                Useraccount useraccount = virtualAccount.getUserAccount();
                String accountName = Strings.capitalize(userService.getUserFullName(useraccount));
                log.info("attempting to update account name of reserved account before reassigning");

                JSONObject body2 = new JSONObject();
                body2.put("account_number", assignedAccount.getAccountNumber());
                body2.put("account_name", accountName);
                log.info("Request body is : {}", body2);

                log.info("Reassigning Reserved Account with current name {} to new name of : {}",
                        assignedAccount.getAccountName(),
                        accountName);

                URL = BASE_URL + configurationService
                        .getByKey(ConfigKeyType.PROVIDUS_UPDATE_ACCOUNT_NAME_URL).getConfigValue();

                log.info(URL);

                HttpEntity<String> request = new HttpEntity<>(body2.toString(), Helper.getProvidusHeaders(CLIENTID, X_AUTH_SIGNATURE));

//                log.info("request is : {}", request);

                ResponseEntity<ProvidusDynamicAccountResponseDto> response = restTemplate
                        .exchange(URL, HttpMethod.POST, request,
                                ProvidusDynamicAccountResponseDto.class);

                log.info("Response obtained from providus when trying to update reserved virtual account name is : " + response);

                if (Helper.isNotEmpty(response)
                        && Helper.isNotEmpty(response.getBody())
                        && response.getBody().getRequestSuccessful().equals(Boolean.TRUE)
                        && Helper.isNotEmpty(response.getBody().getResponseCode())
                        && response.getBody().getResponseCode().equals("00")) {
                    log.info("reassigning name...");
                    virtualAccount.setAccountName("ROSABON(" + accountName + ")");
                    virtualAccount = virtualAccountDao.save(virtualAccount);
                    log.info("reassigned virtual account name is : {}", virtualAccount.getAccountName());
                }else {
                    log.info("PiPUpdateAccountName call was not successful so we are not updating");
                }
            });
        }
    }

//    @EventListener(ApplicationStartedEvent.class)
    public void changeNameOnReservedVirtualAccounts () {
        init();
        if (activeProfile.equals("prod")) {
            List<UserVirtualAccount> reservedVirtualAccounts;
            List<UserVirtualAccount> duplicatesToDelete = new ArrayList<>();
            reservedVirtualAccounts = virtualAccountDao.findAllByAccountType(UserVirtualAccount.VirtualAccountType.RESERVED);
            reservedVirtualAccounts.forEach(virtualAccount -> {
                List<UserVirtualAccount> duplicateVirtualAccounts = virtualAccountDao.findAllByAccountNumberOrderByIdDesc(virtualAccount.getAccountNumber());
                if (duplicateVirtualAccounts.size() > 1) {
                    log.info("{} found for virtual account with number {}",duplicateVirtualAccounts.size(),virtualAccount.getAccountNumber());
                    for (int i = 1; i < duplicateVirtualAccounts.size(); i++ ) {
                        duplicatesToDelete.add(duplicateVirtualAccounts.get(i));
                    }
                }
            });
            log.info("size of the list of duplicates to delete is : {}",duplicatesToDelete.size());
            virtualAccountDao.deleteAll(duplicatesToDelete);
            log.info("duplicates deleted");

            log.info("changing name on reserved virtual account");
            reservedVirtualAccounts = virtualAccountDao.findAllByAccountType(UserVirtualAccount.VirtualAccountType.RESERVED);
            reservedVirtualAccounts.forEach(virtualAccount -> {
                log.info("virtual account number in question : {}", virtualAccount.getAccountNumber());
                log.info("virtual account id is : {}", virtualAccount.getId());
                log.info("current virtual account name is : {}", virtualAccount.getAccountName());
                Useraccount useraccount = virtualAccount.getUserAccount();
                String accountName = Strings.capitalize(userService.getUserFullName(useraccount));
                log.info("attempting to update account name of reserved account before reassigning");

                JSONObject body2 = new JSONObject();
                body2.put("account_number", virtualAccount.getAccountNumber());
                body2.put("account_name", accountName);
                log.info("Request body is : {}", body2);

                log.info("Reassigning Reserved Account with current name {} to new name of : {}",
                        virtualAccount.getAccountName(),
                        accountName);

                URL = BASE_URL + configurationService
                        .getByKey(ConfigKeyType.PROVIDUS_UPDATE_ACCOUNT_NAME_URL).getConfigValue();

                log.info(URL);

                HttpEntity<String> request = new HttpEntity<>(body2.toString(), Helper.getProvidusHeaders(CLIENTID, X_AUTH_SIGNATURE));

//                log.info("request is : {}", request);

                ResponseEntity<ProvidusDynamicAccountResponseDto> response;

                try {
                    response = restTemplate
                            .exchange(URL, HttpMethod.POST, request,
                                    ProvidusDynamicAccountResponseDto.class);
                }catch (Exception e) {
                    log.info("exception encountered was : {} at {}",e.getMessage(),e.getStackTrace()[0]);
                    return;
                }

                log.info("Response obtained from providus when trying to update reserved virtual account name is : " + response);

                if (Helper.isNotEmpty(response)
                        && Helper.isNotEmpty(response.getBody())
                        && response.getBody().getRequestSuccessful().equals(Boolean.TRUE)
                        && Helper.isNotEmpty(response.getBody().getResponseCode())
                        && response.getBody().getResponseCode().equals("00")) {
                    log.info("updating name...");
                    virtualAccount.setAccountName("ROSABON(" + accountName + ")");
                    virtualAccount = virtualAccountDao.save(virtualAccount);
                    log.info("updated virtual account name is : {}", virtualAccount.getAccountName());

//                    try {
//                        List<UserVirtualAccountReserved> assignedAccounts = userVirtualAccountReservedDao.findAllByAccountNumber(virtualAccount.getAccountNumber());
//                        if (!assignedAccounts.isEmpty()) {
//                            log.info("reassigning unassigned account while updating name");
//                            UserVirtualAccountReserved a = assignedAccounts.get(0);
//                            log.info("unassigned account id is {} :",a);
//                            a.setStatus(EntityStatus.ASSIGNED);
//                            a.setUserAccountId(virtualAccount.getUserAccount().getId());
//                            userVirtualAccountReservedDao.save(a);
//                        }
//                    }catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }else {
                    log.info("PiPUpdateAccountName call was not successful so we are not updating");
                }
            });
        }
    }

    private void checkForDuplicateVirtualAccountNumber(ResponseEntity<ProvidusDynamicAccountResponseDto> response) {
        String accountNumber = Objects.requireNonNull(response.getBody()).getAccount_number();
        List<UserVirtualAccount> virtualAccounts = virtualAccountDao.findAllByAccountNumber(accountNumber);

        if (!virtualAccounts.isEmpty()) {
            virtualAccounts.forEach(virtualAccount -> {
                log.info("An existing virtual account was found:\naccount name: {}, account number: {}",
                        virtualAccount.getAccountName(), virtualAccount.getAccountNumber());
            });
            throw new BadRequestException("A virtual account with the same account number already exists");
        }
    }

    @Override
    public HttpResponseDto verifyTransactionBySettlementId(String settlementId) {

        if (Helper.isEmpty(settlementId)) {
            log.warn(
                    "Settlement Id is required");
            throw new BadRequestException(
                    "Settlement Id is required");
        }


        log.info("Verify Transaction by Settlement Id: {}", settlementId);
//        URL = BASE_URL + "/PiPverifyTransaction_settlementid?settlement_id=" + settlementId;
//        log.info(URL);
        URL = BASE_URL + configurationService
                .getByKey(ConfigKeyType.PROVIDUS_VERIFY_TRANSACTION_BY_SETTLEMENT_ID_URL).getConfigValue() + settlementId;
        log.info(URL);
        HttpEntity<String> request = new HttpEntity(Helper.getProvidusHeaders(CLIENTID, X_AUTH_SIGNATURE));
        log.info(request.toString());
        ResponseEntity<ProvidusVerifyTransactionResponseDto> response = restTemplate
                .exchange(URL, HttpMethod.GET, request,
                        ProvidusVerifyTransactionResponseDto.class);
        if (Helper.isNotEmpty(response.getBody())) {
            log.info("Verify Transaction went successful");


            return new HttpResponseDto(APIMessage.SUCCESSFUL_REQUEST.concat(
                    "Transaction has been verified."),
                    HttpStatus.OK, "Verify", response);
        }
        throw new BadRequestException(
                request.toString());

    }

    @Override
    public HttpResponseDto verifyTransactionBySessionId(String sessionId) {

        if (Helper.isEmpty(sessionId)) {
            log.warn(
                    "Settlement Id is required");
            throw new BadRequestException(
                    "Settlement Id is required");
        }

        log.info("Verify Transaction by Session Id: {}", sessionId);
//        URL = BASE_URL + "/PiPverifyTransaction_sessionid?session_id=" + sessionId;
//        log.info(URL);

        URL = BASE_URL + configurationService
                .getByKey(ConfigKeyType.PROVIDUS_VERIFY_TRANSACTION_BY_SESSION_ID_URL).getConfigValue() + sessionId;
        log.info(URL);
        HttpEntity<String> request = new HttpEntity(Helper.getProvidusHeaders(CLIENTID, X_AUTH_SIGNATURE));
        log.info(request.toString());
        ResponseEntity<ProvidusVerifyTransactionResponseDto> response = restTemplate
                .exchange(URL, HttpMethod.GET, request,
                        ProvidusVerifyTransactionResponseDto.class);
        if (Helper.isNotEmpty(response.getBody())) {
            log.info("Verify Transaction went successful");


            return new HttpResponseDto(APIMessage.SUCCESSFUL_REQUEST.concat(
                    "Transaction has been verified."),
                    HttpStatus.OK, "Verify", response);
        }
        throw new BadRequestException(
                request.toString());


    }

    //we need to break this down
    @Override
    public ProvidusResponseDTO settlementNotification(String XAuthSignature, ProvidusVerifyTransactionResponseDto requestDto, String settlementId) {
        log.info("Settlement Notification received on providus callback");
        log.info("Body is :\n" + requestDto);
        init();
        ProvidusResponseDTO response = new ProvidusResponseDTO(
                requestDto.getSessionId(), true,
                ProvidusResponseMessage.REJECTED_TRANSACTION.getMessage(),
                ProvidusResponseCode.REJECTED_TRANSACTION.getCode());

        if (!(XAuthSignature.equalsIgnoreCase(X_AUTH_SIGNATURE))) {
            log.info("Bad Request: X_AUTH_SIGNATURE does not match");
//            log.info("Providus signature : {}   ", XAuthSignature);
//            log.info("Our Own signature : {}", X_AUTH_SIGNATURE);
            response.setRequestSuccessful(true);
            response.setResponseCode(ProvidusResponseCode.REJECTED_TRANSACTION.getCode());
            response.setResponseMessage(ProvidusResponseMessage.REJECTED_TRANSACTION.getMessage());
            log.info("Settlement Notification attended to, elapsed time {}", System.currentTimeMillis() - startTime);
            applicationEventPublisher.publishEvent(
                    new AuditTrailEvent(this,
                            new CreateAuditTrailDto(
                                    AuditTrail.Category.PROVIDUS,
                                    "Bad Request: X_AUTH_SIGNATURE does not match for sessionId : " + requestDto.getSessionId() + " settlement id : " + requestDto.getSettlementId() + " account number : " + requestDto.getAccountNumber(),
                                    null,
                                    null,
                                    null)
                    )
            );
            return response;
        }
//        Check if the request has settlementId and the settleAMount is not negative
        if (
                Helper.isEmpty(requestDto.getSettlementId())
                        || (requestDto.getSettledAmount() < 1)
                        || (requestDto.getTransactionAmount() < 1)//added check for to transaction amount
                        || Helper.isEmpty(requestDto.getAccountNumber())
                        || Helper.isEmpty(requestDto.getTranDateTime())
        ) {
            log.info("Some fields were missing::");
            response.setResponseCode(ProvidusResponseCode.REJECTED_TRANSACTION.getCode());
            response.setResponseMessage(ProvidusResponseMessage.REJECTED_TRANSACTION.getMessage());
            log.info("Settlement Notification attended to, elapsed time {}", System.currentTimeMillis() - startTime);
            applicationEventPublisher.publishEvent(
                    new AuditTrailEvent(this,
                            new CreateAuditTrailDto(
                                    AuditTrail.Category.PROVIDUS,
                                    "Some fields were missing for sessionId : " + requestDto.getSessionId() + " settlement id : " + requestDto.getSettlementId() + " account number : " + requestDto.getAccountNumber(),
                                    null,
                                    null,
                                    null)
                    )
            );
            return response;
        }

        log.info("checking if settlement id already exists");
        if (planActionContract.settlementIdAlreadyExists(requestDto.getSettlementId())) {
            response.setRequestSuccessful(true);
            response.setResponseMessage(ProvidusResponseMessage.DUPLICATE_TRANSACTION.getMessage());
            response.setResponseCode(ProvidusResponseCode.DUPLICATE_TRANSACTION.getCode());
            log.info("settlement id already exists so we are returning response meant for duplicate");
            log.info("Settlement Notification attended to, elapsed time {}", System.currentTimeMillis() - startTime);
            applicationEventPublisher.publishEvent(
                    new AuditTrailEvent(this,
                            new CreateAuditTrailDto(
                                    AuditTrail.Category.PROVIDUS,
                                    "settlement id already exists for sessionId : " + requestDto.getSessionId() + " settlement id : " + requestDto.getSettlementId() + " account number : " + requestDto.getAccountNumber(),
                                    null,
                                    null,
                                    null)
                    )
            );
            return response;
        }

        log.info("settlement id does not exist. We can proceed");

        //Check if the virtualAccount exists in our system
        UserVirtualAccount virtualAccount;
        try {
            log.info("checking for virtual account that has the account number received");
            virtualAccount = userVirtualAccountService.findByAccountNumber(requestDto.getAccountNumber());
        } catch (Exception e) {
            log.info("duplicate virtual accounts with same account number were probably found");
            response.setRequestSuccessful(true);
            response.setResponseMessage(ProvidusResponseMessage.REJECTED_TRANSACTION.getMessage());
            response.setResponseCode(ProvidusResponseCode.REJECTED_TRANSACTION.getCode());
            if (!providusReversalDao.existsBySettlementId(requestDto.getSettlementId())
                    && !providusReversalDao.existsBySessionId(requestDto.getSessionId())) {
                log.info("queuing plan for reversal");
                ProvidusReversal providusReversal = ProvidusReversal.builder()
                        .payloadReceived(Helper.convertToJson(requestDto))
                        .settlementId(requestDto.getSettlementId())
                        .sessionId(requestDto.getSessionId())
                        .amountToReverse(requestDto.getTransactionAmount())
                        .errorEncountered("Error : duplicate account not recognized")
                        .virtualAccount(requestDto.getAccountNumber())
                        .reversalStatus(ProvidusReversal.ReversalStatus.PENDING)
                        .build();
                providusReversalDao.save(providusReversal);
            }
            applicationEventPublisher.publishEvent(
                    new AuditTrailEvent(this,
                            new CreateAuditTrailDto(
                                    AuditTrail.Category.PROVIDUS,
                                    "duplicate virtual accounts for sessionId : "
                                            + requestDto.getSessionId()
                                            + " settlement id : "
                                            + requestDto.getSettlementId()
                                            + " account number : "
                                            + requestDto.getAccountNumber(),
                                    null,
                                    null,
                                    null)
                    )
            );
            e.printStackTrace();
            return response;
        }

        log.info("Verifying Virtual Account Number ::" + requestDto.getAccountNumber());
        if (Helper.isEmpty(virtualAccount)) {
            log.info("Error Virtual Account not Recognized ::" + virtualAccount);
            applicationEventPublisher.publishEvent(
                    new AuditTrailEvent(this,
                            new CreateAuditTrailDto(
                                    AuditTrail.Category.PROVIDUS,
                                    "Error Virtual Account not Recognized for sessionId : " + requestDto.getSessionId() + " settlement id : " + requestDto.getSettlementId() + " account number : " + requestDto.getAccountNumber(),
                                    null,
                                    null,
                                    null)
                    )
            );
            response.setResponseCode(ProvidusResponseCode.REJECTED_TRANSACTION.getCode());
            response.setResponseMessage(ProvidusResponseMessage.REJECTED_TRANSACTION.getMessage());
            //queue for refund customer here
            if (!providusReversalDao.existsBySettlementId(requestDto.getSettlementId())
                    && !providusReversalDao.existsBySessionId(requestDto.getSessionId())) {
                log.info("queuing plan for reversal");
                ProvidusReversal providusReversal = ProvidusReversal.builder()
                        .payloadReceived(Helper.convertToJson(requestDto))
                        .settlementId(requestDto.getSettlementId())
                        .sessionId(requestDto.getSessionId())
                        .amountToReverse(requestDto.getTransactionAmount())
                        .errorEncountered("Error : virtual account not recognized")
                        .virtualAccount(requestDto.getAccountNumber())
                        .reversalStatus(ProvidusReversal.ReversalStatus.PENDING)
                        .build();
                providusReversalDao.save(providusReversal);
            }
            log.info("Transaction has been queued for reversal");
            log.info("Settlement Notification attended to, elapsed time {}", System.currentTimeMillis() - startTime);
            return response;
        }

        //Get User from account
        Useraccount user = virtualAccount.getUserAccount();
        TrPlan plan = virtualAccount.getPlan();

        if (Helper.isEmpty(user) && Helper.isEmpty(plan)) {
            log.info("Error : Virtual account not connected with any user or plan::" + user);
            applicationEventPublisher.publishEvent(
                    new AuditTrailEvent(this,
                            new CreateAuditTrailDto(
                                    AuditTrail.Category.PROVIDUS,
                                    "Error : Virtual account not connected with any user or plan for sessionId : " + requestDto.getSessionId() + " settlement id : " + requestDto.getSettlementId() + " account number : " + requestDto.getAccountNumber(),
                                    user,
                                    null,
                                    null)
                    )
            );
            response.setRequestSuccessful(true);
            response.setResponseCode(ProvidusResponseCode.REJECTED_TRANSACTION.getCode());
            response.setResponseMessage(ProvidusResponseMessage.REJECTED_TRANSACTION.getMessage());
            //queue for refund customer here
            if (!providusReversalDao.existsBySettlementId(requestDto.getSettlementId())
                    && !providusReversalDao.existsBySessionId(requestDto.getSessionId())) {
                log.info("queuing transaction for reversal");
                ProvidusReversal providusReversal = ProvidusReversal.builder()
                        .payloadReceived(Helper.convertToJson(requestDto))
                        .settlementId(requestDto.getSettlementId())
                        .sessionId(requestDto.getSessionId())
                        .amountToReverse(requestDto.getTransactionAmount())
                        .errorEncountered("Virtual account not connected with any user or plan")
                        .virtualAccount(requestDto.getAccountNumber())
                        .reversalStatus(ProvidusReversal.ReversalStatus.PENDING)
                        .build();
                providusReversalDao.save(providusReversal);
            }
            log.info("Transaction has been queued for reversal");
            log.info("Settlement Notification attended to, elapsed time {}", System.currentTimeMillis() - startTime);
            return response;
        }

        if (user != null) log.info("User Found:: " + user.getEmail());
        if (plan != null) log.info("Plan Found:: " + plan.getPlanName());

        // String payee = requestDto.getSourceAccountName() + " | " + requestDto.getSourceAccountNumber() + " | "+ requestDto.getSourceBankName();
        String payee = requestDto.getTranRemarks();
        log.info("Payee: {}", payee);

        if (virtualAccount.getAccountType() == UserVirtualAccount.VirtualAccountType.DYNAMIC) {
            log.info("virtual account is a dynamic account");
            String accountName = virtualAccount.getAccountName();
            log.info("Account Name {}", accountName);
            String[] planNamePlanIdActionLog = accountName.split("_R_");
            if (planNamePlanIdActionLog.length == 3) {
                log.info("proceeding to complete top up");
                try {
                    planActionContract.completeTopUpAfterBankTransferReceived(
                            planNamePlanIdActionLog,
                            requestDto.getTransactionAmount(),//changed from settlement amount to transaction amount
                            requestDto.getSettlementId()
                    );
                }catch (Exception e) {
                    e.printStackTrace();
                    //queue for refund customer here
                    log.info("Could not complete top up ::");
                    response.setRequestSuccessful(true);
                    response.setResponseCode(ProvidusResponseCode.REJECTED_TRANSACTION.getCode());
                    response.setResponseMessage(ProvidusResponseMessage.REJECTED_TRANSACTION.getMessage());
                    if (!providusReversalDao.existsBySettlementId(requestDto.getSettlementId())
                            && !providusReversalDao.existsBySessionId(requestDto.getSessionId())) {
                        applicationEventPublisher.publishEvent(
                                new AuditTrailEvent(this,
                                        new CreateAuditTrailDto(
                                                AuditTrail.Category.PROVIDUS,
                                                "Could not complete top up for sessionId : " + requestDto.getSessionId() + " settlement id : " + requestDto.getSettlementId(),
                                                user,
                                                null,
                                                null)
                                )
                        );
                        log.info("queuing transaction for reversal");
                        ProvidusReversal providusReversal = ProvidusReversal.builder()
                                .payloadReceived(Helper.convertToJson(requestDto))
                                .settlementId(requestDto.getSettlementId())
                                .sessionId(requestDto.getSessionId())
                                .amountToReverse(requestDto.getTransactionAmount())
                                .errorEncountered("Could not complete top up")
                                .virtualAccount(requestDto.getAccountNumber())
                                .planName(planNamePlanIdActionLog[0].replace("ROSABON(", "")
                                        .replace("MERCHANT(", ""))
                                .useraccount(user)
                                .reversalStatus(ProvidusReversal.ReversalStatus.PENDING)
                                .build();
                        providusReversalDao.save(providusReversal);
                    }
                    log.info("Settlement Notification attended to, elapsed time {}", System.currentTimeMillis() - startTime);
                    return response;
                }
            }
            else if (!planActionContract.activatePlan(virtualAccount, requestDto.getTransactionAmount())) {
                log.info("Could not activate plan ::");
                log.info("sending in app and email notification");
                String title = "PLAN ACTIVATION FAILED";
                String userFullName = userService.getUserFullName(user);
                String message = "Dear " + userFullName +
                        "\nYour attempt to activate up your " +
                        "plan with " + requestDto.getTransactionAmount() + " failed";
                InAppEvent event = new InAppEvent();
                event.setDateSent(LocalDateTime.now());
                event.setMessage(message);
                event.setInitiatorUserId(null);
                event.setRecipientUserId(user.getId());

                event.setTitle(title);
                event.setPlatform(GenericModuleBaseEntity.Platform.TREASURY);
                event.setCategory(NotificationCategory.PLAN_ACTION);
                notify.createInApp(event);

                EmailTemplateDto templateDto = new EmailTemplateDto();
                templateDto.setSubject(title);
                templateDto.setMailTo(user.getEmail());
                Map<String, String> props = new HashMap<>();
                props.put("firstName", userFullName);
                props.put("message", event.getMessage());
                templateDto.setProps(props);
                emailService.sendNotification(templateDto);
                response.setResponseCode(ProvidusResponseCode.REJECTED_TRANSACTION.getCode());
                response.setResponseMessage(ProvidusResponseMessage.REJECTED_TRANSACTION.getMessage());
                //queue for refund customer here
                if (!providusReversalDao.existsBySettlementId(requestDto.getSettlementId())
                        && !providusReversalDao.existsBySessionId(requestDto.getSessionId())) {
                    applicationEventPublisher.publishEvent(
                            new AuditTrailEvent(this,
                                    new CreateAuditTrailDto(
                                            AuditTrail.Category.PROVIDUS,
                                            "Could not activate plan for sessionId : " + requestDto.getSessionId() + " settlement id : " + requestDto.getSettlementId(),
                                            user,
                                            null,
                                            null)
                            )
                    );
                    response.setRequestSuccessful(true);
                    log.info("queuing transaction for reversal");
                    ProvidusReversal providusReversal = ProvidusReversal.builder()
                            .payloadReceived(Helper.convertToJson(requestDto))
                            .settlementId(requestDto.getSettlementId())
                            .sessionId(requestDto.getSessionId())
                            .amountToReverse(requestDto.getTransactionAmount())
                            .errorEncountered("Could not activate plan")
                            .virtualAccount(requestDto.getAccountNumber())
                            .planName(plan != null ? plan.getPlanName() : null)
                            .useraccount(user)
                            .reversalStatus(ProvidusReversal.ReversalStatus.PENDING)
                            .build();
                    providusReversalDao.save(providusReversal);
                }
                log.info("Settlement Notification attended to, elapsed time {}", System.currentTimeMillis() - startTime);
                return response;
            }
            virtualAccountDao.delete(virtualAccount);
        } else if (virtualAccount.getAccountType() == UserVirtualAccount.VirtualAccountType.RESERVED) {
            log.info("virtual account is a reserved account");
            try {
                log.info("crediting wallet");
                //Rejecting existing settlement id
                synchronized (this) {
                    Optional<WalletTransactionHistory> walletTransactionHistoryOp = walletTransactionHistoryDao
                            .findBySettlementId(settlementId);
                    if(walletTransactionHistoryOp.isPresent()) {
                        response.setRequestSuccessful(true);
                        response.setResponseMessage(ProvidusResponseMessage.DUPLICATE_TRANSACTION.getMessage());
                        response.setResponseCode(ProvidusResponseCode.DUPLICATE_TRANSACTION.getCode());
                        return  response;
                    }
                    walletMngr.creditWallet(user,
                            requestDto.getTransactionAmount(),//changed from settlement amount to transaction amount
                            WalletTransaction.TransactionCategory.BANK_ACCOUNT_TO_WALLET_FUNDING,
                            payee, settlementId,
                            true,
                            false);
                }

                log.info("sending in app and email notification");
                String title = "INFLOW TO WALLET";
                String userFullName = userService.getUserFullName(user);
                String message = "Great news! Your wallet has been successfully topped up with N" + requestDto.getTransactionAmount() + ".<br><br>" +
                        "There is a lot you can do with your funds. Whether you want to create a new investment or top-up " +
                        "your investments with the funds in your wallet, we are here to ensure you are well-equipped to " +
                        "achieve financial freedom.<br><br>" +
                        "If you have any questions or need assistance along the way, our dedicated support team is ready " +
                        "to assist you. Kindly reach out to us by sending a mail to clientservices@rosabon-finance.com " +
                        "or call us on 0700ROSABON (07007672266).<br>";

                InAppEvent event = new InAppEvent();
                event.setDateSent(LocalDateTime.now());
                event.setMessage(message);
                event.setInitiatorUserId(null);
                event.setRecipientUserId(user.getId());

                event.setTitle(title);
                event.setPlatform(GenericModuleBaseEntity.Platform.TREASURY);
                event.setCategory(NotificationCategory.WALLET);
                notify.createInApp(event);

                EmailTemplateDto templateDto = new EmailTemplateDto();
                templateDto.setSubject(title);
                templateDto.setMailTo(user.getEmail());
                Map<String, String> props = new HashMap<>();
                props.put("firstName", userFullName);
                props.put("message", event.getMessage());
                templateDto.setProps(props);
//                emailService.sendNotification(templateDto);
                applicationEventPublisher.publishEvent(
                        new EmailEvent(this,
                                templateDto, GenericModuleBaseEntity.Platform.TREASURY
                        )
                );
            }catch (Exception e) {
                response.setRequestSuccessful(true);
                log.info("debiting wallet since an error occurred and the transaction has been marked for reversal");
               synchronized (this) {
                   Optional<WalletTransactionHistory> historyOptional = walletTransactionHistoryDao.findBySettlementId(settlementId);
                   if (historyOptional.isPresent()) {
                       WalletTransactionHistory history = historyOptional.get();
                       history.setSettlementId(null);

                       walletTransactionHistoryDao.save(history);
                   }
                   walletMngr.debitWallet(user,
                           requestDto.getTransactionAmount(),//changed from settlement amount to transaction amount
                           WalletTransaction.TransactionCategory.BANK_ACCOUNT_TO_WALLET_FUNDING,
                           payee, null);
               }
                e.printStackTrace();
                //queue for refund customer here
                log.info("Could not credit wallet ::");
                response.setResponseCode(ProvidusResponseCode.REJECTED_TRANSACTION.getCode());
                response.setResponseMessage(ProvidusResponseMessage.REJECTED_TRANSACTION.getMessage());
                if (!providusReversalDao.existsBySettlementId(requestDto.getSettlementId())
                        && !providusReversalDao.existsBySessionId(requestDto.getSessionId())) {
                    applicationEventPublisher.publishEvent(
                            new AuditTrailEvent(this,
                                    new CreateAuditTrailDto(
                                            AuditTrail.Category.PROVIDUS,
                                            "Could not credit wallet : " + requestDto.getSessionId() + " settlement id : " + requestDto.getSettlementId(),
                                            user,
                                            null,
                                            null)
                            )
                    );
                    log.info("queuing transaction for reversal");
                    ProvidusReversal providusReversal = ProvidusReversal.builder()
                            .payloadReceived(Helper.convertToJson(requestDto))
                            .settlementId(requestDto.getSettlementId())
                            .sessionId(requestDto.getSessionId())
                            .amountToReverse(requestDto.getTransactionAmount())
                            .errorEncountered(e.getMessage())
                            .virtualAccount(requestDto.getAccountNumber())
                            .useraccount(user)
                            .reversalStatus(ProvidusReversal.ReversalStatus.PENDING)
                            .build();
                    providusReversalDao.save(providusReversal);
                }
                log.info("Settlement Notification attended to, elapsed time {}", System.currentTimeMillis() - startTime);
                return response;
            }
        }
        log.info("Settlement Notification attended to, elapsed time {}", System.currentTimeMillis() - startTime);
        applicationEventPublisher.publishEvent(
                new AuditTrailEvent(this,
                        new CreateAuditTrailDto(
                                AuditTrail.Category.PROVIDUS,
                                "virtual account top up for : "
                                        + virtualAccount.getAccountNumber()
                                        + " sessionId : " + requestDto.getSessionId()
                                        + " settlement id : "
                                        + requestDto.getSettlementId()
                                        + " amount : "
                                        + requestDto.getTransactionAmount(),
                                user,
                                null,
                                null)
                )
        );
        response.setRequestSuccessful(true);
        response.setResponseCode(ProvidusResponseCode.SUCCESS.getCode());
        response.setResponseMessage(ProvidusResponseMessage.SUCCESS.getMessage());
        return response;
    }

//    private void attemptRefundToSourceAccount(ProvidusVerifyTransactionResponseDto requestDto) {
    @Scheduled(cron = "0 0/8 * * * *", zone = "Africa/Lagos")
    private void attemptRefundToSourceAccount() {
        log.info("attempting to refund customers for rejected providus transactions");
        init();
        URL = BASE_URL + "PiPreturnTransaction";
        log.info(URL);
        List<ProvidusReversal> providusReversals = providusReversalDao.findAllByReversalStatusAndNoOfReversalAttemptsLessThan(
                ProvidusReversal.ReversalStatus.PENDING,
                5
        );
        log.info("{} reversals queued",providusReversals.size());
        providusReversals.forEach(providusReversal -> {
            ProvidusReverseTranxReq reverseTranxReq = new ProvidusReverseTranxReq(
                    providusReversal.getSettlementId(),
                    providusReversal.getSessionId()
            );
            HttpEntity<ProvidusReverseTranxReq> request = new HttpEntity<>(
                    reverseTranxReq,
                    Helper.getProvidusHeaders(CLIENTID, X_AUTH_SIGNATURE)
            );
            log.info("request is :\n{}", request);
            ResponseEntity<ProvidusReverseTranxResp> reverseTranxResp = null;
            try {
                reverseTranxResp = restTemplate
                        .exchange(
                                URL,
                                HttpMethod.POST,
                                request,
                                ProvidusReverseTranxResp.class
                        );
            }catch (Exception e) {
                e.printStackTrace();
            }
            ProvidusReverseTranxResp providusReverseTranxResp = null;
            if (!Helper.isEmpty(reverseTranxResp)) {
                providusReverseTranxResp = reverseTranxResp.getBody();
                log.info("response from reversal call is : {}",providusReverseTranxResp);
                if (!Helper.isEmpty(providusReverseTranxResp)
                        && providusReverseTranxResp.isRequestSuccessful()
                        && providusReverseTranxResp.getResponseCode()
                        .equalsIgnoreCase(ProvidusResponseCode.SUCCESS.getCode())
//                        && providusReverseTranxResp.getResponseMessage()
//                        .equalsIgnoreCase("Transaction has been returned to the merchant's settlement account successfully; Please give value to customer.")
                ) {
                    log.info("reversal with id {} was successful",providusReversal.getId());
                    providusReversal.setReversalStatus(ProvidusReversal.ReversalStatus.REVERSED);
                    providusReversal.setReversalTime(LocalDateTime.now());
                }
            }
            providusReversal.setReversalAttemptResponse(Helper.convertToJson(providusReverseTranxResp));
            providusReversal.setNoOfReversalAttempts(providusReversal.getNoOfReversalAttempts()+1);
        });
        log.info("updating reversals...");
        try {
            providusReversalDao.saveAll(providusReversals);
        }catch (Exception e) {
            log.info("error encountered was :\n {}", e.getMessage());
        }
    }

    @Override
    public HttpResponseDto repushSettlementNotification(String settlementId, String sessionId) {

        if (Helper.isEmpty(sessionId) && Helper.isEmpty(settlementId)) {
            throw new BadRequestException(
                    "settlementId or sessionId must be present");

        }

        log.info("Calling for a repush: SessionId: {} settlementId: {}", sessionId, settlementId);
//        URL = BASE_URL + "PiP_RepushTransaction_SettlementId";
//        URL = "https://vps.providusbank.com/vps/api/PiP_RepushTransaction_SettlementId";

        URL = BASE_URL + configurationService
                .getByKey(ConfigKeyType.PROVIDUS_SETTLEMENT_REPUSH_URL).getConfigValue() + sessionId;
        log.info(URL);
        JSONObject data = new JSONObject();
        try {
            data.put("settlement_id", settlementId);
            data.put("session_id", sessionId);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        HttpEntity<String> req = new HttpEntity(data.toString(), Helper.getProvidusHeaders(CLIENTID, X_AUTH_SIGNATURE));
        log.info("Body {}", req.toString());

        ResponseEntity<String> response = restTemplate
                .exchange(URL, HttpMethod.POST, req, String.class);

        System.out.println(response);
//        if (Helper.isNotEmpty(response.getBody()) && response.getBody().isRequestSuccessful()) {
//            log.info("Verify Transaction went successful");


        return new HttpResponseDto(APIMessage.SUCCESSFUL_REQUEST.concat(
                "Transaction has been set for repush."),
                HttpStatus.OK, "providus", response.getBody());
    }

    @Override
    public UserVirtualAccount createDynamicAccount(Useraccount user, String accountName) {

        throw new BadRequestException("This payment method is not available. Please use other payment methods");

//        init();
//
//        JSONObject body = new JSONObject();
//        try {
//
//            body.put("account_name", StringUtils.capitalize(accountName));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        log.info("account name of dynamic account is : {}", body);
////        URL = BASE_URL + "/PiPCreateDynamicAccountNumber";
////        log.info(URL);
//        URL = BASE_URL + configurationService
//                .getByKey(ConfigKeyType.PROVIDUS_CREATE_DYNAMIC_ACCOUNT_NUMBER_URL).getConfigValue();
//        log.info("providus endpoint url we are calling is {}", URL);
//
//        HttpEntity<String> request = new HttpEntity(body.toString(), Helper.getProvidusHeaders(CLIENTID, X_AUTH_SIGNATURE));
//        log.info("request to create dynamic account is :\n {}", request);
//        ResponseEntity<ProvidusDynamicAccountResponseDto> response = restTemplate
//                .exchange(URL, HttpMethod.POST, request,
//                        ProvidusDynamicAccountResponseDto.class);
//        log.info("response from providus is :\n {}", response);
//        if (Helper.isNotEmpty(response.getBody()) && response.getBody().getRequestSuccessful().equals(Boolean.TRUE) && Helper.isNotEmpty(response.getBody().getAccount_number())) {
//            log.info("Create Dynamic Account Number went successful");
//            log.info(response.toString());
////            log.info("Update the account name removing Merchant()");
////            boolean isupdateSuccessful = updateAccountName(user.getFullName(), response.getBody().getAccount_number());
////
////            if (isupdateSuccessful) {
////                response.getBody().setAccount_name(user.getFullName());
////                log.info("Confirming Update {}", response.getBody().toString());
////
////            }
////            Useraccount user = userDao.findByEmail(auth.getName());
//
//            checkForDuplicateVirtualAccountNumber(response);
//
//            UserVirtualAccount userVirtualAccount = userVirtualAccountService.create(
//                    response.getBody(),
//                    user,
//                    UserVirtualAccount.VirtualAccountType.DYNAMIC);
//
//            if (Helper.isNotEmpty(userVirtualAccount)) {
//                String userFullName = userService.getUserFullName(user);
//
//                String message = "Account details expires in 2 hours "
//                        + "\nkindly endeavour to make transfer before " + LocalDateTime.now().plusHours(3);
//
//                InAppEvent event = new InAppEvent();
//                event.setDateSent(LocalDateTime.now());
//                event.setMessage(message);
//                event.setInitiatorUserId(null);
//                event.setRecipientUserId(user.getId());
//                event.setTitle("Dynamic Account Created");
//                event.setPlatform(GenericModuleBaseEntity.Platform.TREASURY);
//                event.setCategory(NotificationCategory.PLAN_ACTION);
//                notify.createInApp(event);
//
//                return userVirtualAccount;
//            }
//        }
//        return null;
    }
}
