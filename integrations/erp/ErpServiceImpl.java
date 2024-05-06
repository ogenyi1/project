package ng.optisoft.rosabon.integrations.erp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ng.optisoft.rosabon.credit.clients.HttpClientCaller;
import ng.optisoft.rosabon.credit.dao.CrLoanDao;
import ng.optisoft.rosabon.credit.dao.CrLoanRepaymentHistoryDao;
import ng.optisoft.rosabon.credit.enums.LoanStatus;
import ng.optisoft.rosabon.credit.model.CRLoan;
import ng.optisoft.rosabon.credit.model.CRLoanRepaymentHistory;
import ng.optisoft.rosabon.dto.EmailTemplateDto;
import ng.optisoft.rosabon.dto.response.HttpResponseDto2;
import ng.optisoft.rosabon.enums.NotificationCategory;
import ng.optisoft.rosabon.event.InAppEvent;
import ng.optisoft.rosabon.exception.NotFoundException;
import ng.optisoft.rosabon.integrations.erp.dto.request.ErpActionsReq;
import ng.optisoft.rosabon.integrations.erp.dto.request.ErpTransactionStatusReq;
import ng.optisoft.rosabon.integrations.erp.dto.request.UpdateLoanRepaymentReq;
import ng.optisoft.rosabon.integrations.erp.dto.response.ErpActionsResponse;
import ng.optisoft.rosabon.integrations.erp.dto.response.ErpTransactionStatusResp;
import ng.optisoft.rosabon.model.GenericModuleBaseEntity;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.service.EmailService;
import ng.optisoft.rosabon.service.NotificationMngr;
import ng.optisoft.rosabon.service.UseraccountMngr;
import ng.optisoft.rosabon.util.CrUtil;
import ng.optisoft.rosabon.util.Helper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ErpServiceImpl implements ErpService {

    private final String ENTITY = "CR_LOAN";
    private final CrLoanDao loanDao;
    private final CrLoanRepaymentHistoryDao crLoanRepaymentHistoryDao;
    private final HttpClientCaller httpClientCaller;
    @Value("${erp-withdraw-loan-request-url}")
    private String withdrawLoanRequestUrl;
    @Value("${erp-repayment-sync-url}")
    private String erpRepaymentSyncUrl;
    @Value("${erp-transaction-status-url}")
    private String erpTransactionStatusUrl;
    private final UseraccountMngr useraccountMngr;
    private final NotificationMngr notify;
    private final EmailService emailService;

    @Override
    public ResponseEntity<HttpResponseDto2> receiveRepaymentInfo(UpdateLoanRepaymentReq updateLoanRepaymentReq) {
        try {
            log.info("received callback in receiveRepaymentInfo() on {}", new Date());
            log.info("********** updateLoanRepaymentReq :\n{}", updateLoanRepaymentReq);
            //process repayment info
            //Get loan by transaction id
            CRLoan loan = loanDao.findByTransactionId(updateLoanRepaymentReq.getTransactionID())
                    .orElseThrow(() -> new NotFoundException("loan with transaction ID " + updateLoanRepaymentReq.getTransactionID() + " not found"));
            loan.setRepayment(
                    Objects.requireNonNullElse(loan.getRepayment(),BigDecimal.ZERO)
                            .add(BigDecimal.valueOf(
                                    Double.parseDouble(updateLoanRepaymentReq.getAmount())))
            );
            //Create new repayment history
            //set fields of repayment history
            //Add to list of repayment history
            //reset repayment history of loan
            //save loan
            CRLoanRepaymentHistory loanRepaymentHistory = CRLoanRepaymentHistory.builder()
                    .amount(BigDecimal.valueOf(Double.parseDouble(updateLoanRepaymentReq.getAmount())))
                    .loan(loan)
                    .build();
            crLoanRepaymentHistoryDao.save(loanRepaymentHistory);
            return new ResponseEntity<>(
                    new HttpResponseDto2(
                            HttpStatus.OK,
                            ENTITY,
                            "repayment info processed"
                    ),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(new HttpResponseDto2(HttpStatus.EXPECTATION_FAILED, ENTITY, "processing repayment info failed", e.getMessage(), e.getStackTrace()[0].toString(), null), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @Override
    public ResponseEntity<HttpResponseDto2> withdrawLoanRequest(String transactionId) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        Map<String, String> urlParams = new HashMap<>();
//        urlParams.put("transaction-id", transactionId);
//
//        HttpEntity<String> request = new HttpEntity<>(headers);
//
//        ResponseEntity<String> response;
//
//        try {
//            response = restTemplate.exchange(withdrawLoanRequestUrl, HttpMethod.POST, request, String.class, urlParams);
//            log.info("response from request to withdraw loan request is : {}", response);
//        } catch (Exception e) {
//            return new ResponseEntity<>(new HttpResponseDto2(HttpStatus.EXPECTATION_FAILED, ENTITY, "attempting to withdraw loan request failed", e.getMessage(), e.getStackTrace()[0].toString(), null), HttpStatus.EXPECTATION_FAILED);
//        }
//
//        if (response.getStatusCode() == HttpStatus.OK) {
//            //find loan by transaction id
//            log.info("--------------------------------response status code was OK--------------------------------");
//            CRLoan loan = loanDao.findByTransactionId(transactionId).orElseThrow(() -> new NotFoundException("loan with id : " + transactionId + "was not found"));
//            //set status to closed
//            loan.setLoanStatus(LoanStatus.WITHDRAWN);
//            //save loan
//            loanDao.save(loan);
//            //update customer
//            return new ResponseEntity<>(new HttpResponseDto2(HttpStatus.OK, ENTITY, "loan request withdrawn"), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(new HttpResponseDto2(HttpStatus.EXPECTATION_FAILED, ENTITY, "attempting to withdraw loan request failed", "response status was " + response.getStatusCode(), null, null), HttpStatus.EXPECTATION_FAILED);
//        }
        return null;
    }

    @Override
    public ResponseEntity<ErpActionsResponse> receiveErpActions(ErpActionsReq erpActionsReq) {
        String transactionID = erpActionsReq.getTransactionID();
        ErpActionsResponse response = ErpActionsResponse.builder()
                .transactionId(transactionID)
                .message("OK")
                .build();
        try {
            log.info("received callback in receiveErpActions() on {}", new Date());
            log.info("********** erpActionsReq :\n{}", erpActionsReq);

            //get loan by transaction id
            Optional<CRLoan> loanOptional= loanDao.findByTransactionId(transactionID);
            if (loanOptional.isEmpty()) {
                log.info("transaction id was not found");
            }else {
                CRLoan loan = loanOptional.get();
                Useraccount user = loan.getUseraccount();
                String userFullName = useraccountMngr.getUserFullName(user);
                String title = "LOAN PROCESSING UPDATE";
                String message = "";

                InAppEvent event = new InAppEvent();
                event.setDateSent(LocalDateTime.now());
                event.setInitiatorUserId(null);
                event.setRecipientUserId(user.getId());
                event.setTitle(title);
                event.setPlatform(GenericModuleBaseEntity.Platform.CREDIT);
                event.setCategory(NotificationCategory.LOAN);

                EmailTemplateDto templateDto = new EmailTemplateDto();
                templateDto.setSubject(title);
                templateDto.setMailTo(user.getEmail());
                Map<String, String> props = new HashMap<>();
                props.put("firstName", userFullName);
                templateDto.setProps(props);

                ErpTransactionStatusReq transactionStatusReq = ErpTransactionStatusReq.builder()
                        .transactionID(Long.valueOf(transactionID))
                        .build();

                String transactionStatusRespString;

                try {
                    transactionStatusRespString = httpClientCaller.makeGenericPostRequest(
                            erpTransactionStatusUrl,
                            transactionStatusReq);
                }catch (Exception e) {
                    e.printStackTrace();
                    return new ResponseEntity<>(
                            response,
                            HttpStatus.OK
                    );
                }

                ErpTransactionStatusResp transactionStatusResp = CrUtil.convertJsonStringToObject(
                        transactionStatusRespString,
                        ErpTransactionStatusResp.class);

                log.info("{}",transactionStatusResp);

                String transactionStatus = transactionStatusResp.getStatus();

//                if (transactionStatus.equalsIgnoreCase(ErpTransactionStatusResp.ErpTransactionStatus.APPROVED.toString())) {
//                    Helper.log("loan is approved");
//                    Helper.log("updating loan status");
//                    loan.setLoanStatus(LoanStatus.APPROVED);
//                    log.info("transaction has been pushed forward to the next stage");
//                    message = "Dear " + userFullName + "\n "
//                            + "Your loan application has been approved by an underwriter and is set to be disbursed";
//                } else if (transactionStatus.equalsIgnoreCase(ErpTransactionStatusResp.ErpTransactionStatus.DISBURSED.toString())) {
//                    Helper.log("loan is disbursed");
//                    Helper.log("updating loan status");
//                    loan.setLoanStatus(LoanStatus.DISBURSED);
//                    loan.setDisbursementDate(LocalDateTime.now());
//                    message = "Dear " + userFullName + "\n "
//                            + "Your loan has been disbursed.\n" +
//                            "Kindly log on to your profile for the details.";
//                }
//                else if (transactionStatus.equalsIgnoreCase(ErpTransactionStatusResp.ErpTransactionStatus.TERMINATED.toString())) {
//                    Helper.log("loan is terminated");
//                    Helper.log("updating loan status");
//                    loan.setLoanStatus(LoanStatus.DECLINED);
//                    message = "Dear " + userFullName + "\n "
//                            + "Unfortunately, your loan application has been declined.\n" +
//                            "Reason : " + erpActionsReq.getPushReason();
//                }
//                else if (transactionStatus.equalsIgnoreCase(ErpTransactionStatusResp.ErpTransactionStatus.BLACKLISTED.toString())) {
//                    Helper.log("loan is blacklisted");
//                    Helper.log("updating loan status");
//                    loan.setLoanStatus(LoanStatus.DECLINED);
//                    message = "Dear " + userFullName + "\n "
//                            + "Unfortunately, your loan application has been declined.\n" +
//                            "Reason : Transaction is blacklisted";
//                }
//                else if (transactionStatus.equalsIgnoreCase(ErpTransactionStatusResp.ErpTransactionStatus.ACTIVE.toString())) {
//                    Helper.log("loan is active");
//                    Helper.log("updating loan status");
//                    loan.setLoanStatus(LoanStatus.RUNNING);
//                    message = "Dear " + userFullName + "\n "
//                            + "Your loan is now active.\n" +
//                            "Kindly log on to your profile for the details.";
//                }
                if (transactionStatus.equalsIgnoreCase(ErpTransactionStatusResp.ErpTransactionStatus.PENDING.toString())) {
                    Helper.log("loan is pending");
                    message =
//                            "Dear " + userFullName + "\n "
//                            +
                            "Your pending loan request is being reviewed by our underwriters.<br>"
                            + "A " + erpActionsReq.getAction() + " has been taken on your request.<br>\n"
                            + "We will keep you updated.<br>";
                }
                if (erpActionsReq.getAction().equalsIgnoreCase("down")
                        && transactionStatusResp.getCurrentStep().equalsIgnoreCase("Booking")) {
                    log.info("setting loan request to DECLINED in webhook");
                    loan.setLoanStatus(LoanStatus.DECLINED);
                    loanDao.save(loan);
                    title = "LOAN PROCESSING UPDATE - APPLICATION DECLINED";
                    templateDto.setSubject(title);
                    message =
//                            "Dear " + userFullName + "\n "
//                            +
                            "Your pending loan request is being DECLINED by our underwriters.<br>" +
                            "Reason : " + erpActionsReq.getPushReason() + "<br>";
                }

//                if (erpActionsReq.getAction().equals(ErpAction.PUSH_FORWARD.getAction())) {
//                    log.info("transaction has been pushed forward to the next stage");
//                    message = "Dear " + userFullName + "\n "
//                            + "Your loan has been moved to the next underwriting stage";
//
//                    //track if loan has been disbursed and set a boolean that loan has been disbursed
//                    //notify user
//                } else if (erpActionsReq.getAction().equals(ErpAction.PUSH_BACK.getAction())) {
//                    log.info("transaction has been pushed back to the previous underwriting stage");
//                    message = "Dear " + userFullName + "\n "
//                            + "Your loan has been pushed back by the underwriter";
//                    //track if loan has been declined and set a boolean that loan has been declined
//                    //notify user
//                }
                props.put("message", message);
                event.setTitle(title);
                event.setMessage(message);
                notify.createInApp(event);
                emailService.sendNotificationCredit(templateDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    response,
                    HttpStatus.OK
            );
        }
        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<HttpResponseDto2> sendRepaymentInfo(UpdateLoanRepaymentReq updateLoanRepaymentReq) {
        log.info("sending repayment info");
        String stringResponse;
        try {
            stringResponse = httpClientCaller.makeGenericPostRequest(
                    erpRepaymentSyncUrl,
                    updateLoanRepaymentReq
            );
        }catch (Exception e) {
            return new ResponseEntity<>(
                    new HttpResponseDto2(
                            HttpStatus.EXPECTATION_FAILED,
                            ENTITY,
                            "sending repayment info to ERP failed",
                            e.getMessage(),
                            e.getStackTrace()[0].toString(),
                            null
                    ),
                    HttpStatus.EXPECTATION_FAILED
            );
        }
        log.info("response from call is : {}",stringResponse);
        return new ResponseEntity<>(
                new HttpResponseDto2(
                        HttpStatus.OK,
                        ENTITY,
                        "repayment info sent to ERP",
                        null
                ),
                HttpStatus.OK
        );
    }

    //cron to move transaction to running after underwriting stage on ERP
    @Scheduled(cron = "0 */15 * * * *", zone = "Africa/Lagos")
    public void checkForLoanStatus () {
        Helper.log("checking for loan statuses");
//        List<CRLoan> loans = loanDao.findAllByLoanStatusIn(List.of(
//                LoanStatus.APPROVED,
//                LoanStatus.DISBURSED,
//                LoanStatus.RUNNING));
        List<CRLoan> loans = loanDao.findAllByLoanStatusIn(List.of(
                LoanStatus.INITIALIZING,
                LoanStatus.PROCESSING,
                LoanStatus.DOCUMENT_UPLOAD_REQUIRED));
        loans.forEach(loan -> {
            if (loan.getTransactionId() != null) {
                ErpTransactionStatusReq transactionStatusReq = ErpTransactionStatusReq.builder()
                        .transactionID(Long.valueOf(loan.getTransactionId()))
                        .build();

                String transactionStatusRespString;

                try {
                    transactionStatusRespString = httpClientCaller.makeGenericPostRequest(
                            erpTransactionStatusUrl,
                            transactionStatusReq);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                ErpTransactionStatusResp transactionStatusResp = CrUtil.convertJsonStringToObject(
                        transactionStatusRespString,
                        ErpTransactionStatusResp.class);

                String transactionStatus = transactionStatusResp.getStatus();

//            if (loan.getLoanStatus().equals(LoanStatus.COMPLETED)
//                    || loan.getLoanStatus().equals(LoanStatus.WITHDRAWN)
//                    || loan.getLoanStatus().equals(LoanStatus.DECLINED)
//                    || loan.getLoanStatus().equals(LoanStatus.DISBURSED)) {
//                if (transactionStatus.equalsIgnoreCase(ErpTransactionStatusResp.ErpTransactionStatus.ACTIVE.toString())
//                        && !loan.getLoanStatus().equals(LoanStatus.RUNNING)) {
//                    loan.setLoanStatus(LoanStatus.RUNNING);
//                } else if (transactionStatus.equalsIgnoreCase(ErpTransactionStatusResp.ErpTransactionStatus.BLACKLISTED.toString())
//                        && !loan.getLoanStatus().equals(LoanStatus.DECLINED)) {
//                    loan.setLoanStatus(LoanStatus.DECLINED);
//                } else if (transactionStatus.equalsIgnoreCase(ErpTransactionStatusResp.ErpTransactionStatus.DELETED.toString())
//                        && !loan.getLoanStatus().equals(LoanStatus.DECLINED)) {
//                    loan.setLoanStatus(LoanStatus.DECLINED);
//                } else if (transactionStatus.equalsIgnoreCase(ErpTransactionStatusResp.ErpTransactionStatus.TERMINATED.toString())
//                        && !loan.getLoanStatus().equals(LoanStatus.WITHDRAWN)) {
//                    loan.setLoanStatus(LoanStatus.WITHDRAWN);
//                } else if (transactionStatus.equalsIgnoreCase(ErpTransactionStatusResp.ErpTransactionStatus.COMPLETED.toString())
//                        && !loan.getLoanStatus().equals(LoanStatus.COMPLETED)) {
//                    loan.setLoanStatus(LoanStatus.COMPLETED);
//                }
//            }else {
//                Helper.log("loan is " + loan.getLoanStatus() + " on the credit portal so we will not be modifying");
//            }

                Useraccount user = loan.getUseraccount();
                log.info("transaction status from ERP was : {}", transactionStatus);
                log.info("loan status on credit portal was : {}", loan.getLoanStatus());
                String userFullName = useraccountMngr.getUserFullName(user);
                if (transactionStatus.equalsIgnoreCase(ErpTransactionStatusResp.ErpTransactionStatus.COMPLETED.toString())
                        || transactionStatus.equalsIgnoreCase(ErpTransactionStatusResp.ErpTransactionStatus.DISBURSED.toString())) {
                    String title = "LOAN PROCESSING UPDATE";

                    InAppEvent event = new InAppEvent();
                    event.setDateSent(LocalDateTime.now());
                    event.setInitiatorUserId(null);
                    event.setRecipientUserId(user.getId());
                    event.setTitle(title);
                    event.setPlatform(GenericModuleBaseEntity.Platform.CREDIT);
                    event.setCategory(NotificationCategory.LOAN);

                    EmailTemplateDto templateDto = new EmailTemplateDto();
                    templateDto.setSubject(title);
                    templateDto.setMailTo(user.getEmail());
                    Map<String, String> props = new HashMap<>();
                    props.put("firstName", userFullName);
                    templateDto.setProps(props);
                    loan.setLoanStatus(LoanStatus.RUNNING);
                    log.info("transaction has been set to running on credit portal");
                    String message = "Dear " + userFullName + "\n "
                            + "Your loan application is now running";
                    props.put("message", message);
                    event.setMessage(message);
                    notify.createInApp(event);
                    emailService.sendNotificationCredit(templateDto);
                }
            }

        });

        loanDao.saveAll(loans);
    }
}
