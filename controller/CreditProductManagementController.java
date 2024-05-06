package ng.optisoft.rosabon.credit.controller;

import lombok.RequiredArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.credit.dto.request.*;
import ng.optisoft.rosabon.credit.service.*;
import ng.optisoft.rosabon.dto.HttpResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
public class CreditProductManagementController {
    private final CrTargetMarketService targetMarketService;
    private final CrProductCategoryService productCategoryService;
    private final CrProductTypeService productTypeService;
    private final CrTopUpMgtService topUpMgtService;
    private final CrInterestTypeService interestTypeService;
//    private final CrRepaymentTypeMgtService repaymentTypeMgtService;
    private final CrGroupCompanyMgtService crGroupCompanyMgtService;
    private final CrVehicleMgtService vehicleMgtService;
    private final CrIndustryMgtService industryMgtService;
    private final CrProfOccupationService profOccupationService;
    private final CrRegistrationMgtService crRegistrationMgtService;
    private final CrLiquidationService liquidationService;
    private final CrPenalChargeService penalChargeService;
    private final CrCompanyInclusionService companyInclusionService;
    private final CrPerfectionService perfectionService;
    private final CrEtixService etixService;

    private final CRProductService productService;

    private final CrLoanReasonService loanReasonService;


    @GetMapping(ApiRoute.ADMIN + ApiRoute.PRODUCT + "/{id}")
    public HttpResponseDto getProductById(@Valid @NotNull @PathVariable("id") Long id) {
        return productService.getById(id);
    }
    @GetMapping(ApiRoute.ADMIN + ApiRoute.ETIX + "/{id}")
    public HttpResponseDto getEtixMgtById(@Valid @NotNull @PathVariable("id") Long id) {
        return etixService.getById(id);
    }
    @GetMapping(ApiRoute.ADMIN + ApiRoute.GROUP_COMPANY + "/{id}")
    public HttpResponseDto getGroupCompanyById(@Valid @NotNull @PathVariable("id") Long id) {
        return crGroupCompanyMgtService.getById(id);
    }
    @GetMapping(ApiRoute.ADMIN + ApiRoute.INTEREST_TYPE + "/{id}")
    public HttpResponseDto getInterestTypeById(@Valid @NotNull @PathVariable("id") Long id) {
        return interestTypeService.getById(id);
    }
    @GetMapping(ApiRoute.ADMIN + ApiRoute.LOAN_REASON + "/{id}")
    public HttpResponseDto getByLoanReasonId(@Valid @NotNull @PathVariable("id") Long id) {
        return loanReasonService.getById(id);
    }
    @GetMapping(ApiRoute.ADMIN + ApiRoute.PERFECTION + "/{id}")
    public HttpResponseDto getPerfectionById(@Valid @NotNull @PathVariable("id") Long id) {
        return perfectionService.getById(id);
    }

    @GetMapping(ApiRoute.ADMIN + ApiRoute.PENAL_CHARGE + "/{id}")
    public HttpResponseDto getPenalChargeById(@Valid @NotNull @PathVariable("id") Long id) {
        return penalChargeService.getById(id);
    }
    @GetMapping(ApiRoute.ADMIN + ApiRoute.PRODUCT_CATEGORY + "/{id}")
    public HttpResponseDto getProductCategoryById(@Valid @NotNull @PathVariable("id") Long id) {
        return productCategoryService.getById(id);
    }


    @GetMapping(ApiRoute.ADMIN + ApiRoute.PROF_OCCUPATION + "/{id}")
    public HttpResponseDto getProfOccupationById(@Valid @NotNull @PathVariable("id") Long id) {
        return profOccupationService.getById(id);
    }
    @GetMapping(ApiRoute.ADMIN + ApiRoute.PRODUCT_TYPE + "/{id}")
    public HttpResponseDto getProductType(@Valid @NotNull @PathVariable("id") Long id) {
        return productTypeService.getById(id);
    }
//    @GetMapping(ApiRoute.ADMIN + ApiRoute.REPAYMENT_TYPE + "/{id}")
//    public HttpResponseDto getRepaymentById(@Valid @NotNull @PathVariable("id") Long id) {
//        return repaymentTypeMgtService.getById(id);
//    }

    @GetMapping(ApiRoute.ADMIN + ApiRoute.REGISTRATION_MGT + "/{id}")
    public HttpResponseDto getRegistrationById(@Valid @NotNull @PathVariable("id") Long id) {
        return crRegistrationMgtService.getById(id);
    }
//    @GetMapping(ApiRoute.ADMIN + ApiRoute.REQUIREMENT + "/{id}")
//    public HttpResponseDto getRequirementMgt(@Valid @NotNull @PathVariable("id") Long id) {
//        return requirementMgtService.getById(id);
//    }
    @GetMapping(ApiRoute.ADMIN + ApiRoute.TARGET_MARKET + "/{id}")
    public HttpResponseDto getTargetMarketById(@Valid @NotNull @PathVariable("id") Long id) {
        return targetMarketService.getById(id);
    }
    @GetMapping(ApiRoute.ADMIN + ApiRoute.VEHICLE_MGT + "/{id}")
    public HttpResponseDto getVehicleMgtById(@Valid @NotNull @PathVariable("id") Long id) {
        return vehicleMgtService.getById(id);
    }

    @GetMapping(ApiRoute.ADMIN + ApiRoute.TOP_UP + "/{id}")
    public HttpResponseDto getTopUpById(@Valid @NotNull @PathVariable("id") Long id) {
        return topUpMgtService.getById(id);
    }
    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.GET_ALL_CREDIT_PRODUCT)
    public ResponseEntity<?> getAllCreditProducts(@PageableDefault(sort = "id", direction = DESC)Pageable pageable){
        return ResponseEntity.ok().body(productService.getAllCreditProduct(pageable));
    }

    @GetMapping(value = ApiRoute.ADMIN + "/{uuid}" +ApiRoute.GET_INCLUSION)
    public ResponseEntity<?> getCompanyTotalWeightAndAvgScoreForAuditCrmAndLease(@PathVariable String uuid){
        return ResponseEntity.ok().body(companyInclusionService.calculateCompanyTotalWeightAndAvgScoreForAuditCrmAndLease(uuid));
    }

    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.GET_TARGET_MARKET)
    public ResponseEntity<?> getAllTargetMarket(@PageableDefault(sort = "id", direction = DESC) Pageable pageable){
        return ResponseEntity.ok().body(targetMarketService.getAllTargetMarket(pageable));
    }

    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.GET_PRODUCT_CODES)
    public ResponseEntity<?> getAllProductCodes(){
        return ResponseEntity.ok().body(productService.getAllProductCodes());
    }

//    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.GET_LOAN_REQUIREMENT)
//    public ResponseEntity<?> getLoanRequirementChecklist(){
//        return ResponseEntity.ok().body(requirementMgtService.getAllRequirementChecklist());
//    }

//    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.GET_LOAN_REQUIREMENT)
//    public ResponseEntity<?> getLoanRequirementChecklist(@PageableDefault(sort = "id", direction = DESC) Pageable pageable){
//        return ResponseEntity.ok().body(requirementMgtService.getAllRequirement(pageable));
//    }

    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.GET_COMPANY_INCL)
    public ResponseEntity<?> getAllCompanyInclusion(@PageableDefault(sort = "id", direction = DESC) Pageable pageable){
        return ResponseEntity.ok().body(companyInclusionService.getAllCompanyInclusion(pageable));
    }

    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.GET_PRODUCT_CATEGORY)
    public ResponseEntity<?> getAllProductCategory(@PageableDefault(sort = "id", direction = DESC) Pageable pageable){
        return ResponseEntity.ok().body(productCategoryService.getAllProductCategory(pageable));
    }

    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.GET_INTEREST_TYPE)
    public ResponseEntity<?> getAllInterestType(@PageableDefault(sort = "id", direction = DESC) Pageable pageable){
        return ResponseEntity.ok().body(interestTypeService.getAllInterestType(pageable));
    }

    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.GET_ALL_TOP_UP)
    public ResponseEntity<?> getAllTopUp(@PageableDefault(sort = "id", direction = DESC) Pageable pageable){
        return ResponseEntity.ok().body(topUpMgtService.getAllTopUp(pageable));
    }

//    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.GET_REPAYMENT_TYPE)
//    public ResponseEntity<?> getAllRepaymentType(@PageableDefault(sort = "id", direction = DESC) Pageable pageable){
//        return ResponseEntity.ok().body(repaymentTypeMgtService.getAllRepaymentType(pageable));
//    }

    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.GET_PERFECTION)
    public ResponseEntity<?> getAllPerfection(@PageableDefault(sort = "id", direction = DESC) Pageable pageable){
        return ResponseEntity.ok().body(perfectionService.getAllPerfection(pageable));
    }

    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.GET_ETIX)
    public ResponseEntity<?> getAllEtix(@PageableDefault(sort = "id", direction = DESC) Pageable pageable){
        return ResponseEntity.ok().body(etixService.getAllEtix(pageable));
    }

    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.GET_LIQUIDATION)
    public ResponseEntity<?> getAllLiquidation(@PageableDefault(sort = "id", direction = DESC) Pageable pageable){
        return ResponseEntity.ok().body(liquidationService.getAllLiquidation(pageable));
    }

    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.GET_PENAL)
    public ResponseEntity<?> getPenalCharge(@PageableDefault(sort = "id", direction = DESC) Pageable pageable){
        return ResponseEntity.ok().body(penalChargeService.getAllPenalCharge(pageable));
    }

    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.GET_OCCUPATION)
    public ResponseEntity<?> getProfOccupation(@PageableDefault(sort = "id", direction = DESC) Pageable pageable){
        return ResponseEntity.ok().body(profOccupationService.getAllPerfection(pageable));
    }

    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.GET_GROUP_COMPANY)
    public ResponseEntity<?> getAllGroupCompany(@PageableDefault(sort = "id", direction = DESC) Pageable pageable){
        return ResponseEntity.ok().body(crGroupCompanyMgtService.getAllGroupCompany(pageable));
    }

    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.GET_PRODUCT_TYPE)
    public ResponseEntity<?> getAllProductType(@PageableDefault(sort = "id", direction = DESC)Pageable pageable){
        return ResponseEntity.ok().body(productTypeService.getAllProductType(pageable));
    }

    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.GET_ALL_VEHICLE)
    public ResponseEntity<?> getAllVehicleManagement(@PageableDefault(sort = "id", direction = DESC)Pageable pageable){
        return ResponseEntity.ok().body(vehicleMgtService.getAllVehicleMgt(pageable));
    }

    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.GET_INDUSTRY_MANAGEMENT)
    public ResponseEntity<?> getAllIndustryManagement(@PageableDefault(sort = "id", direction = DESC) Pageable pageable){
        return ResponseEntity.ok().body(industryMgtService.getAllIndustryType(pageable));
    }

    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.GET_ALL_REGISTRATION)
    public ResponseEntity<?> getAllRegistration(@PageableDefault(sort = "id", direction = DESC) Pageable pageable){
        return ResponseEntity.ok().body(crRegistrationMgtService.getAllRegistrationMgt(pageable));
    }

    //POST

    @PostMapping(value = ApiRoute.ADMIN + ApiRoute.ADD_LOAN_REASON)
    public ResponseEntity<?> addLoanReason(@RequestBody @Valid LoanReasonReqDto loanReasonReqDto) {

        return ResponseEntity.ok(loanReasonService.saveLoanReason(loanReasonReqDto));
    }
    @PostMapping(value = ApiRoute.ADMIN + ApiRoute.ADD_PRODUCT)
    public ResponseEntity<?> addCreditProduct(@RequestBody @Valid CRProductRequestDto productRequestDto) {

        return ResponseEntity.ok(productService.saveProduct(productRequestDto));
    }

    @PreAuthorize("hasAnyAuthority('CREATE_TARGET_MARKET', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value = ApiRoute.ADMIN + ApiRoute.ADD_TARGET_MARKET)
    public ResponseEntity<?> addTargetMarket(@Valid @RequestBody TargetMarketReqDto targetMarketReqDto) {
        return ResponseEntity.ok(targetMarketService.saveTargetMarket(targetMarketReqDto));
    }

    @PreAuthorize("hasAnyAuthority('CREATE_PRODUCT_CATEGORY', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value = ApiRoute.ADMIN + ApiRoute.ADD_PRODUCT_CATEGORY)
    public ResponseEntity<?> addProductCategory(@Valid @RequestBody ProductCategoryReqDto productCategoryReqDto) {

        return ResponseEntity.ok(productCategoryService.saveProductCategory(productCategoryReqDto));
    }

    @PreAuthorize("hasAnyAuthority('CREATE_PRODUCT_TYPE', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value = ApiRoute.ADMIN + ApiRoute.ADD_PRODUCT_TYPE)
    public ResponseEntity<?> addProductType(@Valid @RequestBody ProductTypeReqDto reqDto) {

        return ResponseEntity.ok(productTypeService.saveProductType(reqDto));
    }

//    @PreAuthorize("hasAnyAuthority('CREATE_REQUIREMENT', 'SUPER_ADMINISTRATOR')")
//    @PostMapping(value = ApiRoute.ADMIN + ApiRoute.ADD_REQUIREMENT)
//    public ResponseEntity<?> addRequirement(@RequestBody @Valid RequirementReqDto requirementReqDto) {
//
//        return ResponseEntity.ok(requirementMgtService.saveRequirement(requirementReqDto));
//    }

    @PreAuthorize("hasAnyAuthority('CREATE_TOPUP', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value = ApiRoute.ADMIN + ApiRoute.ADD_TOP_UP)
    public ResponseEntity<?> addTopUp(@RequestBody @Valid TopUpReqDto topUpReqDto) {

        return ResponseEntity.ok(topUpMgtService.saveTopUp(topUpReqDto));
    }

    @PreAuthorize("hasAnyAuthority('CREATE_INTEREST_TYPE', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value = ApiRoute.ADMIN + ApiRoute.ADD_INTEREST_TYPE)
    public ResponseEntity<?> addInterestType(@RequestBody @Valid InterestTypeReqDto interestTypeReqDto) {

        return ResponseEntity.ok(interestTypeService.saveInterestType(interestTypeReqDto));
    }

//    @PreAuthorize("hasAnyAuthority('CREATE_REPAYMENT_TYPE', 'SUPER_ADMINISTRATOR')")
//    @PostMapping(value = ApiRoute.ADMIN + ApiRoute.ADD_REPAYMENT_TYPE)
//    public ResponseEntity<?> addRepaymentType(@RequestBody @Valid RepaymentTypeReqDto repaymentTypeReqDto) {
//
//        return ResponseEntity.ok(repaymentTypeMgtService.saveRepaymentType(repaymentTypeReqDto));
//    }

    @PreAuthorize("hasAnyAuthority('CREATE_GROUP_COMPANY', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value = ApiRoute.ADMIN + ApiRoute.ADD_GROUP_COMPANY)
    public ResponseEntity<?> addRGroupCompany(@RequestBody @Valid GroupCompanyReqDto groupCompanyReqDto) {

        return ResponseEntity.ok(crGroupCompanyMgtService.saveGroupCompany(groupCompanyReqDto));
    }

    @PreAuthorize("hasAnyAuthority('CREATE_VEHICLE', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value = ApiRoute.ADMIN + ApiRoute.ADD_VEHICLE)
    public ResponseEntity<?> addVehicle(@RequestBody @Valid VehicleReqDto vehicleReqDto) {

        return ResponseEntity.ok(vehicleMgtService.saveVehicle(vehicleReqDto));
    }

    @PreAuthorize("hasAnyAuthority('CREATE_INDUSTRY', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value = ApiRoute.ADMIN + ApiRoute.ADD_INDUSTRY)
    public ResponseEntity<?> addIndustry(@RequestBody @Valid IndustryMgtReqDto industryMgtReqDto) {

        return ResponseEntity.ok(industryMgtService.saveIndustry(industryMgtReqDto));
    }

    @PreAuthorize("hasAnyAuthority('CREATE_PROFESSIONAL_OCCUPATION', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value = ApiRoute.ADMIN + ApiRoute.ADD_PROF_OCCUPATION)
    public ResponseEntity<?> addProfOccupation(@RequestBody @Valid ProfessionalOccupationReqDto professionalOccupationReqDto) {

        return ResponseEntity.ok(profOccupationService.saveProfOccupation(professionalOccupationReqDto));
    }

    @PreAuthorize("hasAnyAuthority('CREATE_REGISTRATION', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value = ApiRoute.ADMIN + ApiRoute.REGISTER_VEHICLE)
    public ResponseEntity<?> registerVehicle(@RequestBody @Valid VehicleRegistrationDto vehicleRegistrationDto) {

        return ResponseEntity.ok(crRegistrationMgtService.saveRegistration(vehicleRegistrationDto));
    }

    @PreAuthorize("hasAnyAuthority('CREATE_LIQUIDATION', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value = ApiRoute.ADMIN + ApiRoute.ADD_LIQUIDATION)
    public ResponseEntity<?> addLiquidation(@RequestBody @Valid LiquidationReqDto liquidationReqDto) {

        return ResponseEntity.ok(liquidationService.saveLiquidation(liquidationReqDto));
    }

    @PreAuthorize("hasAnyAuthority('CREATE_PENAL_CHARGE', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value = ApiRoute.ADMIN + ApiRoute.ADD_PENAL_CHARGE)
    public ResponseEntity<?> addPenalCharge(@RequestBody @Valid PenalChargeReqDto penalChargeReqDto) {

        return ResponseEntity.ok(penalChargeService.savePenalCharge(penalChargeReqDto));
    }

    @PostMapping(value = ApiRoute.ADMIN + ApiRoute.ADD_COMPANY_INCLUSION)
    public ResponseEntity<?> addCompanyInclusion(@RequestBody @Valid CompanyInclusionReqDto companyInclusionReqDto) {

        return ResponseEntity.ok(companyInclusionService.saveCompanyInclusion(companyInclusionReqDto));
    }

    @PreAuthorize("hasAnyAuthority('CREATE_PERFECTION', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value = ApiRoute.ADMIN + ApiRoute.ADD_PERFECTION)
    public ResponseEntity<?> addPerfection(@RequestBody @Valid PerfectionReqDto perfectionReqDto) {

        return ResponseEntity.ok(perfectionService.savePerfection(perfectionReqDto));
    }

    @PostMapping(value = ApiRoute.ADMIN + ApiRoute.ADD_ETIX)
    public ResponseEntity<?> addEtix(@RequestBody @Valid EtixReqDto etixReqDto) {

        return ResponseEntity.ok(etixService.saveEtix(etixReqDto));
    }


    @PreAuthorize("hasAnyAuthority('EDIT_PRODUCT_TYPE', 'SUPER_ADMINISTRATOR')")
    @PutMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.UPDATE_PRODUCT_TYPE)
    public ResponseEntity<?> updateProductType(@RequestBody @Valid  ProductTypeReqDto reqDto, @PathVariable Long id) {

        return ResponseEntity.ok(productTypeService.updateProductType(id,reqDto));
    }

    @PutMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.UPDATE_COMPANY_INCLUSION)
    public ResponseEntity<?> updateCompanyInclusion(@RequestBody @Valid  CompanyInclusionReqDto reqDto, @PathVariable Long id) {

        return ResponseEntity.ok(companyInclusionService.updateCompanyInclusion(id,reqDto));
    }

    @PreAuthorize("hasAnyAuthority('EDIT_PRODUCT_CATEGORY', 'SUPER_ADMINISTRATOR')")
    @PutMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.UPDATE_PRODUCT_CATEGORY)
    public ResponseEntity<?> updateProductCategory(@RequestBody @Valid  ProductCategoryReqDto reqDto, @PathVariable Long id) {

        return ResponseEntity.ok(productCategoryService.updateProductCategory(id,reqDto));
    }

    @PreAuthorize("hasAnyAuthority('EDIT_TARGET_MARKET', 'SUPER_ADMINISTRATOR')")
    @PutMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.UPDATE_TARGET_MARKET)
    public ResponseEntity<?> updateTargetMarket(@RequestBody @Valid  TargetMarketReqDto reqDto, @PathVariable Long id) {

        return ResponseEntity.ok(targetMarketService.updateTargetMarket(id,reqDto));
    }

    @PutMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.UPDATE_LOAN_REASON)
    public ResponseEntity<?> updateLoanReason(@RequestBody @Valid  LoanReasonReqDto loanReasonReqDto, @PathVariable Long id) {

        return ResponseEntity.ok(loanReasonService.updateLoanReason(id, loanReasonReqDto));
    }
    @PutMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.UPDATE_PRODUCT)
    public ResponseEntity<?> updateProduct(@RequestBody @Valid  CRProductRequestDto productRequestDto, @PathVariable Long id) {

        return ResponseEntity.ok(productService.updateProduct(id, productRequestDto));
    }
    @PutMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.UPDATE_ETIX)
    public ResponseEntity<?> updateEtix(@RequestBody @Valid EtixReqDto etixReqDto, @PathVariable Long id) {

        return ResponseEntity.ok(etixService.updateEtix(id, etixReqDto));
    }

    @PreAuthorize("hasAnyAuthority('EDIT_PERFECTION', 'SUPER_ADMINISTRATOR')")
    @PutMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.UPDATE_PERFECTION)
    public ResponseEntity<?> updatePerfection(@RequestBody @Valid PerfectionReqDto perfectionReqDto, @PathVariable Long id) {

        return ResponseEntity.ok(perfectionService.updatePerfection(id, perfectionReqDto));
    }

    @PreAuthorize("hasAnyAuthority('EDIT_PENAL_CHARGE', 'SUPER_ADMINISTRATOR')")
    @PutMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.UPDATE_PENAL_CHARGE)
    public ResponseEntity<?> updatePenalCharge(@RequestBody @Valid PenalChargeReqDto penalChargeReqDto, @PathVariable Long id) {

        return ResponseEntity.ok(penalChargeService.updatePenalCharge(id, penalChargeReqDto));
    }

    @PreAuthorize("hasAnyAuthority('EDIT_LIQUIDATION', 'SUPER_ADMINISTRATOR')")
    @PutMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.UPDATE_LIQUIDATION)
    public ResponseEntity<?> updateLiquidation(@RequestBody @Valid LiquidationReqDto liquidationReqDto, @PathVariable Long id) {

        return ResponseEntity.ok(liquidationService.updateLiquidation(id, liquidationReqDto));
    }

    @PreAuthorize("hasAnyAuthority('EDIT_REGISTRATION', 'SUPER_ADMINISTRATOR')")
    @PutMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.UPDATE_REGISTER_VEHICLE)
    public ResponseEntity<?> updateVehicleRegistration(@RequestBody @Valid VehicleRegistrationDto vehicleRegistrationDto, @PathVariable Long id) {

        return ResponseEntity.ok(crRegistrationMgtService.updateRegistration(id, vehicleRegistrationDto));
    }

//    @PreAuthorize("hasAnyAuthority('EDIT_REPAYMENT_TYPE', 'SUPER_ADMINISTRATOR')")
//    @PutMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.UPDATE_REPAYMENT_TYPE)
//    public ResponseEntity<?> updateRepaymentType(@RequestBody @Valid RepaymentTypeReqDto repaymentTypeReqDto, @PathVariable Long id) {
//
//        return ResponseEntity.ok(repaymentTypeMgtService.updateRepaymentType(id, repaymentTypeReqDto));
//    }

    @PreAuthorize("hasAnyAuthority('EDIT_INTEREST_TYPE', 'SUPER_ADMINISTRATOR')")
    @PutMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.UPDATE_INTEREST_TYPE)
    public ResponseEntity<?> updateInterest(@RequestBody @Valid InterestTypeReqDto interestTypeReqDto, @PathVariable Long id) {

        return ResponseEntity.ok(interestTypeService.updateInterestType(id, interestTypeReqDto));
    }

    @PreAuthorize("hasAnyAuthority('EDIT_GROUP_COMPANY', 'SUPER_ADMINISTRATOR')")
    @PutMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.UPDATE_GROUP_COMPANY)
    public ResponseEntity<?> updateGroupCompany(@RequestBody @Valid GroupCompanyReqDto groupCompanyReqDto, @PathVariable Long id) {

        return ResponseEntity.ok(crGroupCompanyMgtService.updateGroupCompany(id, groupCompanyReqDto));
    }

//    @PreAuthorize("hasAnyAuthority('EDIT_REQUIREMENT', 'SUPER_ADMINISTRATOR')")
//    @PutMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.UPDATE_REQUIREMENT)
//    public ResponseEntity<?> updateRequirement(@RequestBody @Valid RequirementReqDto requirementReqDto, @PathVariable Long id) {
//
//        return ResponseEntity.ok(requirementMgtService.updateRequirement(id, requirementReqDto));
//    }

    @PreAuthorize("hasAnyAuthority('EDIT_TOPUP', 'SUPER_ADMINISTRATOR')")
    @PutMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.UPDATE_TOP_UP)
    public ResponseEntity<?> updateTopUp(@RequestBody @Valid TopUpReqDto topUpReqDto, @PathVariable Long id) {

        return ResponseEntity.ok(topUpMgtService.updateTopUp(id, topUpReqDto));
    }

    @PreAuthorize("hasAnyAuthority('EDIT_PROFESSIONAL_OCCUPATION', 'SUPER_ADMINISTRATOR')")
    @PutMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.UPDATE_PROF_OCCUPATION)
    public ResponseEntity<?> updateProfOccupation(@RequestBody @Valid ProfessionalOccupationReqDto reqDto, @PathVariable Long id) {

        return ResponseEntity.ok(profOccupationService.updateProfOccupation(id, reqDto));
    }

    @PreAuthorize("hasAnyAuthority('EDIT_INDUSTRY', 'SUPER_ADMINISTRATOR')")
    @PutMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.UPDATE_INDUSTRY)
    public ResponseEntity<?> updateIndustry(@RequestBody @Valid IndustryMgtReqDto reqDto, @PathVariable Long id) {

        return ResponseEntity.ok(industryMgtService.updateIndustry(id, reqDto));
    }

    @PreAuthorize("hasAnyAuthority('EDIT_VEHICLE', 'SUPER_ADMINISTRATOR')")
    @PutMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.UPDATE_VEHICLE)
    public ResponseEntity<?> updateVehicle(@RequestBody @Valid VehicleReqDto reqDto, @PathVariable Long id) {

        return ResponseEntity.ok(vehicleMgtService.updateVehicle(id, reqDto));
    }

    //DELETE

    @PreAuthorize("hasAnyAuthority('DELETE_TARGET_MARKET', 'SUPER_ADMINISTRATOR')")
    @DeleteMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.REMOVE_TARGET_MARKET)
    public ResponseEntity<?> removeTargetMarket(@PathVariable Long id) {

        return ResponseEntity.ok(targetMarketService.removeTargetMarket(id));
    }

    @PreAuthorize("hasAnyAuthority('DELETE_PRODUCT_CATEGORY', 'SUPER_ADMINISTRATOR')")
    @DeleteMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.REMOVE_PRODUCT_CATEGORY)
    public ResponseEntity<?> removeProductCategory(@PathVariable Long id) {

        return ResponseEntity.ok(productCategoryService.removeProductCategory(id));
    }

    @PreAuthorize("hasAnyAuthority('DELETE_PRODUCT_TYPE', 'SUPER_ADMINISTRATOR')")
    @DeleteMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.REMOVE_PRODUCT_TYPE)
    public ResponseEntity<?> removeProductType(@PathVariable Long id) {

        return ResponseEntity.ok(productTypeService.removeProductType(id));
    }

//    @PreAuthorize("hasAnyAuthority('DELETE_REQUIREMENT', 'SUPER_ADMINISTRATOR')")
//    @DeleteMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.REMOVE_PRODUCT_REQUIREMENT)
//    public ResponseEntity<?> removeRequirement(@PathVariable Long id) {
//
//        return ResponseEntity.ok(requirementMgtService.removeRequirement(id));
//    }

    @DeleteMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.REMOVE_TOP_UP)
    public ResponseEntity<?> removeTopUp(@PathVariable Long id) {

        return ResponseEntity.ok(topUpMgtService.removeTopUp(id));
    }

    @PreAuthorize("hasAnyAuthority('DELETE_INTEREST_TYPE', 'SUPER_ADMINISTRATOR')")
    @DeleteMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.REMOVE_INTEREST_TYPE)
    public ResponseEntity<?> removeInterestType(@PathVariable Long id) {

        return ResponseEntity.ok(interestTypeService.removeInterestType(id));
    }

//    @PreAuthorize("hasAnyAuthority('DELETE_REPAYMENT_TYPE', 'SUPER_ADMINISTRATOR')")
//    @DeleteMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.REMOVE_REPAYMENT_TYPE)
//    public ResponseEntity<?> removeRepaymentType(@PathVariable Long id) {
//
//        return ResponseEntity.ok(repaymentTypeMgtService.removeRepaymentType(id));
//    }

    @PreAuthorize("hasAnyAuthority('DELETE_GROUP_COMPANY', 'SUPER_ADMINISTRATOR')")
    @DeleteMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.REMOVE_GROUP_COMPANY)
    public ResponseEntity<?> removeGroupCompany(@PathVariable Long id) {

        return ResponseEntity.ok(crGroupCompanyMgtService.removeGroupCompany(id));
    }

    @PreAuthorize("hasAnyAuthority('DELETE_VEHICLE', 'SUPER_ADMINISTRATOR')")
    @DeleteMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.REMOVE_VEHICLE)
    public ResponseEntity<?> removeVehicle(@PathVariable Long id) {

        return ResponseEntity.ok(vehicleMgtService.removeVehicle(id));
    }

    @PreAuthorize("hasAnyAuthority('DELETE_INDUSTRY', 'SUPER_ADMINISTRATOR')")
    @DeleteMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.REMOVE_INDUSTRY)
    public ResponseEntity<?> removeIndustry(@PathVariable Long id) {

        return ResponseEntity.ok(industryMgtService.removeIndustryType(id));
    }

    @PreAuthorize("hasAnyAuthority('DELETE_REGISTRATION', 'SUPER_ADMINISTRATOR')")
    @DeleteMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.REMOVE_REGISTRATION)
    public ResponseEntity<?> removeRegistration(@PathVariable Long id) {

        return ResponseEntity.ok(crRegistrationMgtService.removeRegistration(id));
    }

    @PreAuthorize("hasAnyAuthority('DELETE_PROFESSIONAL_OCCUPATION', 'SUPER_ADMINISTRATOR')")
    @DeleteMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.REMOVE_PROF_OCCU)
    public ResponseEntity<?> removeProfOccupation(@PathVariable Long id) {

        return ResponseEntity.ok(profOccupationService.removeProfOccupation(id));
    }

    @PreAuthorize("hasAnyAuthority('DELETE_PERFECTION', 'SUPER_ADMINISTRATOR')")
    @DeleteMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.REMOVE_PERFECTION)
    public ResponseEntity<?> removePerfection(@PathVariable Long id) {

        return ResponseEntity.ok(perfectionService.removePerfection(id));
    }

    @DeleteMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.REMOVE_PENAL_CHARGE)
    public ResponseEntity<?> removePenalCharge(@PathVariable Long id) {

        return ResponseEntity.ok(penalChargeService.removePenalCharge(id));
    }

    @DeleteMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.REMOVE_LOAN_REASON)
    public ResponseEntity<?> removeLoanReason(@PathVariable Long id) {

        return ResponseEntity.ok(loanReasonService.removeLoanReason(id));
    }

    @DeleteMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.REMOVE_ETIX)
    public ResponseEntity<?> removeEtix(@PathVariable Long id) {

        return ResponseEntity.ok(etixService.removeEtix(id));
    }

    @DeleteMapping(value = ApiRoute.ADMIN + "/{id}" + ApiRoute.REMOVE_CREDIT_PRODUCT)
    public ResponseEntity<?> removeCreditProduct(@PathVariable Long id) {

        return ResponseEntity.ok(productService.removeProduct(id));
    }
    //embedded, reset
}
