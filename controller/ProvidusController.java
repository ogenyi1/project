package ng.optisoft.rosabon.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dao.UserVirtualAccountDao;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.dto.HttpResponseDto;
import ng.optisoft.rosabon.dto.response.CreateAuditTrailDto;
import ng.optisoft.rosabon.enums.ProvidusResponseCode;
import ng.optisoft.rosabon.enums.ProvidusResponseMessage;
import ng.optisoft.rosabon.event.AuditTrailEvent;
import ng.optisoft.rosabon.exception.BadRequestException;
import ng.optisoft.rosabon.integrations.providus.ProvidusService;
import ng.optisoft.rosabon.integrations.providus.dto.*;
import ng.optisoft.rosabon.model.AuditTrail;
import ng.optisoft.rosabon.model.UserVirtualAccount;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.util.Helper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;

/**
 * @author Elesho John
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(ApiRoute.PROVIDUS_PATH)
@Slf4j
public class ProvidusController {


    private final ProvidusService providusService;

    private final UseraccountDao useraccountDao;

    private final UserVirtualAccountDao userVirtualAccountDao;

    private final ApplicationEventPublisher applicationEventPublisher;

    public ProvidusController(ProvidusService providusService, UseraccountDao useraccountDao, UserVirtualAccountDao userVirtualAccountDao, ApplicationEventPublisher applicationEventPublisher) {
        this.providusService = providusService;
        this.useraccountDao = useraccountDao;
        this.userVirtualAccountDao = userVirtualAccountDao;
        this.applicationEventPublisher = applicationEventPublisher;
    }


    @PostMapping(value = ApiRoute.PROVIDUS_VERIFY_TRANSACTION_URL)
    public ResponseEntity<?> verifyTransaction(Authentication authentication,
                                               HttpServletRequest host,
                                               @Valid @RequestBody ProvidusVerifyTransactionRequestDto requestDto) {
        try {
            HttpResponseDto responseDto = providusService.verifyTransaction(requestDto.getSessionOrSettlementId());
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (BadRequestException ex) {
            throw new BadRequestException(ex.getMessage());
        }

    }

    @PostMapping(value = ApiRoute.PROVIDUS_VERIFY_TRANSACTION_BY_SESSION_ID_URL)
    public ResponseEntity<?> verifyTransactionBySessionId(Authentication authentication,
                                                          HttpServletRequest host,
                                                          @Valid @RequestBody ProvidusVerifyTransactionRequestDto requestDto) {
        try {
            HttpResponseDto responseDto = providusService.verifyTransactionBySessionId(requestDto.getSessionOrSettlementId());
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (BadRequestException ex) {
            throw new BadRequestException(ex.getMessage());
        }

    }


    @PostMapping(value = ApiRoute.PROVIDUS_VERIFY_TRANSACTION_BY_SETTLEMENT_ID_URL)
    public ResponseEntity<?> verifyTransactionBySettlementId(Authentication authentication,
                                                             HttpServletRequest host,
                                                             @Valid @RequestBody ProvidusVerifyTransactionRequestDto requestDto) {
        try {
            HttpResponseDto responseDto = providusService.verifyTransactionBySettlementId(requestDto.getSessionOrSettlementId());
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (BadRequestException ex) {
            throw new BadRequestException(ex.getMessage());
        }

    }

    @PostMapping(value = ApiRoute.PROVIDUS_UPDATE_ACCOUNT_NAME_URL)
    public ResponseEntity<?> updateAccountName(Authentication authentication,
                                               HttpServletRequest host,
                                               @Valid @RequestBody ProvidusUpdateAccountNameRequest dto) {
        try {
            ProvidusResponseDTO responseDto = providusService.updateAccountName(dto.getAccount_name(), dto.getAccount_number());
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (BadRequestException ex) {
            throw new BadRequestException(ex.getMessage());
        }

    }


    @ApiOperation("Create Dynamic Account for plan")
    @PostMapping(value = ApiRoute.PROVIDUS_CREATE_DYNAMIC_ACCOUNT)
    public ResponseEntity<?> createDynamicAccount(Authentication authentication,
                                                  HttpServletRequest host,
                                                  @Valid @RequestParam("planName") String planName) {
        try {
            UserVirtualAccount responseDto = providusService.createDynamicAccount(authentication, planName);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (BadRequestException ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }


    @PostMapping(value = ApiRoute.PROVIDUS_BLACKLIST_ACCOUNT_URL)
    public ResponseEntity<?> blackListAccount(Authentication authentication,
                                              HttpServletRequest host,
                                              @Valid @RequestBody ProvidusBlackListAccountRequestDto requestDto) {
        try {
            HttpResponseDto responseDto = providusService.blacklistAccount(requestDto.getAccount_name(), requestDto.getFlag());
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (BadRequestException ex) {
            throw new BadRequestException(ex.getMessage());
        }

    }


    @PostMapping(value = ApiRoute.PROVIDUS_SETTLEMENT_NOTIFICATION_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> settlementNotification(@RequestHeader(value = "X-Auth-Signature", required = false) String XAuthSignature,
                                                    @Valid @RequestBody ProvidusVerifyTransactionResponseDto requestDto) {

        if (Helper.isEmpty(XAuthSignature)) {
            log.info("Bad Request: X_AUTH_SIGNATURE can not be empty");
            applicationEventPublisher.publishEvent(
                    new AuditTrailEvent(this,
                            new CreateAuditTrailDto(
                                    AuditTrail.Category.PROVIDUS,
                                    "Bad Request: X_AUTH_SIGNATURE empty for sessionId : " + requestDto.getSessionId() + " settlement id : " + requestDto.getSettlementId(),
                                    null,
                                    null,
                                    null)
                    )
            );
            return new ResponseEntity<>(new ProvidusResponseDTO(
                    requestDto.getSessionId(), true,
                    ProvidusResponseMessage.REJECTED_TRANSACTION.getMessage(),
                    ProvidusResponseCode.REJECTED_TRANSACTION.getCode()), HttpStatus.OK);
        }
        try {
            String settlementId = requestDto.getSettlementId();
            ProvidusResponseDTO responseDto = providusService.settlementNotification(XAuthSignature, requestDto, settlementId);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception ex) {
            log.info("exception while processing providus settlement webhook is : {}",ex.getMessage());
            applicationEventPublisher.publishEvent(
                    new AuditTrailEvent(this,
                            new CreateAuditTrailDto(
                                    AuditTrail.Category.PROVIDUS,
                                    "Exception occurred for sessionId : " + requestDto.getSessionId() + " settlement id : " + requestDto.getSettlementId(),
                                    null,
                                    null,
                                    null)
                    )
            );
            return new ResponseEntity<>(new ProvidusResponseDTO(
                    requestDto.getSessionId(), true,
                    ProvidusResponseMessage.REJECTED_TRANSACTION.getMessage(),
                    ProvidusResponseCode.REJECTED_TRANSACTION.getCode()), HttpStatus.OK);

        }

    }

    @GetMapping(value = ApiRoute.ADMIN_PATH + ApiRoute.PROVIDUS_SETTLEMENT_REPUSH_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> repushSettlement(
            Authentication authentication,
            HttpServletRequest host,
            @RequestParam(name = "settlementId", required = false) String settlementId, @RequestParam(name = "sessionId", required = false) String sessionId) {


        try {
            HttpResponseDto responseDto = providusService.repushSettlementNotification(settlementId, sessionId);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (BadRequestException ex) {
            throw new BadRequestException(ex.getMessage());
        }

    }

    @GetMapping(value = ApiRoute.VIRTUAL_ACCOUNT)
    public ResponseEntity<?> getVirtualAccount(Authentication auth) {
        Useraccount user = useraccountDao.findByEmail(auth.getName());

        return ResponseEntity.ok(providusService.getVirtualAccount(user));
    }

    @DeleteMapping(value = "/delete-dynamic-accounts-passed-48hours")
    public ResponseEntity<?> deletingDynamicAccount(Authentication authentication) {
        log.info("Deleting Virtual Accounts older than 48 hours");
        userVirtualAccountDao.deleteAllByAccountTypeAndDateAddedIsBefore(UserVirtualAccount.VirtualAccountType.DYNAMIC, LocalDate.now().minusDays(2));
        return new ResponseEntity<>(HttpStatus.OK);


    }

}
