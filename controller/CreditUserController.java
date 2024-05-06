package ng.optisoft.rosabon.credit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.constant.ResourceExtension;
import ng.optisoft.rosabon.credit.dao.CrProductErpDao;
import ng.optisoft.rosabon.credit.dao.CrRepaymentErpDao;
import ng.optisoft.rosabon.credit.dto.EmployeeErpDTO;
import ng.optisoft.rosabon.credit.dto.EmployeeIndustryErpDTO;
import ng.optisoft.rosabon.credit.dto.SectorErpDTO;
import ng.optisoft.rosabon.credit.dto.TestingCloudSave;
import ng.optisoft.rosabon.credit.dto.request.AutoDebitRequest;
import ng.optisoft.rosabon.credit.dto.request.RefundCashRequestDto;
import ng.optisoft.rosabon.credit.dto.request.SubmitLoanRequirementRequest;
import ng.optisoft.rosabon.credit.dto.response.GenericResponse;
import ng.optisoft.rosabon.credit.enums.AssetTypeLease;
import ng.optisoft.rosabon.credit.enums.ResponseCode;
import ng.optisoft.rosabon.credit.externalapis.erp.BasicErpService;
import ng.optisoft.rosabon.credit.externalapis.mybankstatement.BankMyStatementService;
import ng.optisoft.rosabon.credit.externalapis.mybankstatement.dto.ConfirmStatementRequest;
import ng.optisoft.rosabon.credit.externalapis.mybankstatement.dto.GetJsonRequest;
import ng.optisoft.rosabon.credit.model.CRLoan;
import ng.optisoft.rosabon.credit.service.*;
import ng.optisoft.rosabon.credit.service.impl.loanprocessingimpl.LoanBaseService;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.service.impl.BaseServiceImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR', 'INDIVIDUAL_USER', 'COMPANY')")
@Slf4j
public class CreditUserController {

    private final CreditUserService creditUserService;

    private final CrLoanReasonService loanReasonService;

    private final UseraccountDao useraccountDao;

    private final BankMyStatementService bankMyStatementService;

    private final CRProductService productService;

    private final BasicErpService erpService;

    private final CrVehicleMgtService vehicleMgtService;

    private final CrRegistrationMgtService crRegistrationMgtService;

    private final CrErpEntitiesService erpEntitiesService;


    private final CrAutoDebitService autoDebitService;

    private final CrRepaymentErpDao dao;

    private final CrProductErpDao productErpDao;

    @GetMapping(value = ApiRoute.USERS + ApiRoute.GET_ALL_CREDIT_PRODUCT + "/test")
    public ResponseEntity<?> swithOnOffTestinf(@PageableDefault(sort = "id", direction = DESC) Pageable pageable) {
        return ResponseEntity.ok().body(null);

    }

    @GetMapping(value = ApiRoute.USERS + ApiRoute.LEASE)
    public List<AssetTypeLease> getLeaseAssetTypes() {
        return Arrays.asList(AssetTypeLease.values());
    }

    @GetMapping(value = ApiRoute.USERS + ApiRoute.GET_ALL_CREDIT_PRODUCT)
    public ResponseEntity<?> getAllCreditProducts() {
        return ResponseEntity.ok().body(productService.getAllCreditProductNonPaginated());
    }

    @GetMapping(value = ApiRoute.USERS + ApiRoute.ACCESS_LOAN_PRODUCT)
    public ResponseEntity<?> getProductForTargetMarket(Authentication authentication) {
        return ResponseEntity.ok().body(creditUserService.getProductForQualifiedUsers(authentication));
    }

    @GetMapping(value = ApiRoute.USERS + ApiRoute.GET_LOAN_REASON)
    public ResponseEntity<?> getAllLoanReason() {
        return ResponseEntity.ok().body(loanReasonService.getAllLoanReasons());
    }

    @GetMapping(value = ApiRoute.AUTH + ApiRoute.EXISTING_CASHBACKED)
    public ResponseEntity<?> getUserExistingCashBackedLoan(Authentication auth) {

        return creditUserService.getCurrentUserCashBackedLoanDetail(auth);
    }

    @GetMapping(value = ApiRoute.USERS + ApiRoute.GET_ALL_VEHICLE)
    public ResponseEntity<?> getAllVehicleManagement(@PageableDefault(sort = "id", direction = DESC) Pageable pageable) {
        return ResponseEntity.ok().body(vehicleMgtService.getAllVehicleMgt(pageable));
    }

    @GetMapping(value = ApiRoute.USERS + ApiRoute.GET_ALL_REGISTRATION)
    public ResponseEntity<?> getAllRegistration(@PageableDefault(sort = "id", direction = DESC) Pageable pageable) {
        return ResponseEntity.ok().body(crRegistrationMgtService.getAllRegistrationMgt(pageable));
    }

    @GetMapping(value = ApiRoute.USERS + ApiRoute.GET_ERP_EMPLOYERS)
    public ResponseEntity<List<EmployeeErpDTO>> getAllErpEmployers() {
        return erpEntitiesService.getAllErpEmployer();
    }

    @GetMapping(value = ApiRoute.USER + ApiRoute.GET_ERP_EMPLOYERS_INDUSTRY)
    public ResponseEntity<List<EmployeeIndustryErpDTO>> getAllErpEmployersIndustry() {
        return erpEntitiesService.getAllErpEmployerIndustry();
    }


    @GetMapping(value = ApiRoute.USER + ApiRoute.GET_ERP_SECTOR)
    public ResponseEntity<List<SectorErpDTO>> getAllErpSector() {
        return erpEntitiesService.getAllErpSectorIndustry();
    }

    private final LoanBaseService loanBaseService;


    @Operation(
            summary = "Submitting Document Requirement",
            description = "Submit all the required document by providing the base 64 format of each of the listed documents in the getDocumentChecklist endpoint of the crGetCreditController"

    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class)))
    @PostMapping(value = ApiRoute.USERS + "/submit-loan-requirement")
    public ResponseEntity<?> submitLoanDocumentForUnderwriting(Authentication authentication, @RequestBody SubmitLoanRequirementRequest request) {

        return creditUserService.submitLoanDocumentForUnderwriting(authentication, request);

    }

    @PostMapping(value = ApiRoute.USERS + "/statement")
    public ResponseEntity<?> processBankStatement(Authentication authentication) {
        Useraccount useraccount = useraccountDao.findByEmail(authentication.getName());

        var start = LocalDate.of(2022, 1, 1);
        var end = LocalDate.of(2022, 2, 1);

        CRLoan loan = new CRLoan();
        GenericResponse response = bankMyStatementService.requestStatement(null, useraccount, loan, start, end);

        if (!response.getResponseCode().equals(ResponseCode.SUCCESS.getCode()))
            return new ResponseEntity<>(response, HttpStatus.PRECONDITION_FAILED);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping(value = ApiRoute.USERS + "/pdf")
    public ResponseEntity<?> getPdfBankStatement(Authentication authentication, @RequestBody ConfirmStatementRequest request) {
        Useraccount useraccount = useraccountDao.findByEmail(authentication.getName());

        GenericResponse response = bankMyStatementService.getPDFAccountStatement(useraccount, request);

        if (!response.getResponseCode().equals(ResponseCode.SUCCESS.getCode()))
            return new ResponseEntity<>(response, HttpStatus.PRECONDITION_FAILED);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping(value = ApiRoute.USERS + "/json")
    public ResponseEntity<?> getJsonBankStatement(Authentication authentication, @RequestBody GetJsonRequest request) {

        GenericResponse response = bankMyStatementService.handleJSONAccountStatement(request);

        if (!response.getResponseCode().equals(ResponseCode.SUCCESS.getCode()))
            return new ResponseEntity<>(response, HttpStatus.PRECONDITION_FAILED);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping(value = ApiRoute.USERS + "/cloudinary")
    public ResponseEntity<?> testSaveCloudinary(Authentication authentication, @RequestBody TestingCloudSave request) {

        var extension = BaseServiceImpl.handleEnumValidation(ResourceExtension.class, request.getExtension(), "");

        var response = loanBaseService.saveSinglePdfFileViaCloudinary(request.getEncodedString(), extension);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping(value = ApiRoute.USERS + "/checki2")
    public ResponseEntity<?> checksomething21() {
        return ResponseEntity.ok().body(erpService.handleGetErpRepaymentType("16280"));

    }

    @PostMapping(value = ApiRoute.TEST_CARD_TRANSACTION)
    public ResponseEntity<?> testDebitCard(Authentication auth, @RequestBody AutoDebitRequest autoDebitRequest) {
        return ResponseEntity.ok().body(autoDebitService.initializeTestCardPayment(useraccountDao.findByEmail(auth.getName()), autoDebitRequest));
    }

    @PostMapping(value = ApiRoute.TEST_CARD_TRANSACTION + ApiRoute.SEPARATOR + ApiRoute.CREDIT)
    public ResponseEntity<?> testDebitCardCredit(Authentication auth, @RequestBody AutoDebitRequest autoDebitRequest) {
        return ResponseEntity.ok().body(autoDebitService.initializeTestCardPaymentCredit(useraccountDao.findByEmail(auth.getName()), autoDebitRequest));
    }

    @PostMapping(value = ApiRoute.TEST_CASH_REFUND)
    public ResponseEntity<?> refundCash(RefundCashRequestDto requestDto) {
        return ResponseEntity.ok().body(autoDebitService.refundCash(requestDto));
    }

}
