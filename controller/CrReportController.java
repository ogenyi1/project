package ng.optisoft.rosabon.credit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.credit.service.CrReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
@RequestMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.CREDIT + ApiRoute.REPORTS)
@Slf4j
public class CrReportController {
    private final CrReportService crReportService;

    @PostMapping(ApiRoute.GENERATE_CUSTOMER_REPORT)
    public ResponseEntity<?> generateCustomerReport (
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
            @RequestParam(value = "startDate", defaultValue = "1980-01-01T00:00:00")String startDate,
            @RequestParam(value = "endDate", defaultValue = "3000-01-01T00:00:00")String endDate) {
        return crReportService.generateCustomerReport(pageNo,pageSize,sortBy,sortDir,startDate,endDate);
    }

    @PostMapping(ApiRoute.GENERATE_DISBURSEMENT_REPORT)
    public ResponseEntity<?> generateDisbursementReport (
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
            @RequestParam(value = "startDate", defaultValue = "1980-01-01T00:00:00")String startDate,
            @RequestParam(value = "endDate", defaultValue = "3000-01-01T00:00:00")String endDate) {
        return crReportService.generateDisbursementReport(pageNo,pageSize,sortBy,sortDir,startDate,endDate);
    }

    @PostMapping(ApiRoute.GENERATE_PORTFOLIO_REPORT)
    public ResponseEntity<?> generatePortfolioReport (
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
            @RequestParam(value = "startDate", defaultValue = "1980-01-01T00:00:00")String startDate,
            @RequestParam(value = "endDate", defaultValue = "3000-01-01T00:00:00")String endDate) {
        return crReportService.generatePortfolioReport(pageNo,pageSize,sortBy,sortDir,startDate,endDate);
    }

    @PostMapping(ApiRoute.GENERATE_REFERRAL_REPORT)
    public ResponseEntity<?> generateReferralReport (
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
            @RequestParam(value = "startDate", defaultValue = "1980-01-01T00:00:00")String startDate,
            @RequestParam(value = "endDate", defaultValue = "3000-01-01T00:00:00")String endDate) {
        return crReportService.generateReferralReport(pageNo,pageSize,sortBy,sortDir,startDate,endDate);
    }

    @PostMapping(ApiRoute.GENERATE_REFERRAL_BONUS_REPORT_FOR_ACTIVATION)
    public ResponseEntity<?> generateReferralBonusReportForActivation(
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
            @RequestParam(value = "startDate", defaultValue = "1980-01-01T00:00:00")String startDate,
            @RequestParam(value = "endDate", defaultValue = "3000-01-01T00:00:00")String endDate,
            @RequestParam(value = "month", required = false) String month) {
        return crReportService.generateReferralBonusReportForActivation(pageNo,pageSize,sortBy,sortDir,startDate,endDate,month);
    }

    @PostMapping(ApiRoute.GENERATE_REFERRAL_BONUS_REPORT_FOR_REACTIVATION)
    public ResponseEntity<?> generateReferralBonusReportForReActivation(
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
            @RequestParam(value = "startDate", defaultValue = "1980-01-01T00:00:00")String startDate,
            @RequestParam(value = "endDate", defaultValue = "3000-01-01T00:00:00")String endDate,
            @RequestParam(value = "month", required = false) String month) {
        return crReportService.generateReferralBonusReportForReactivation(pageNo,pageSize,sortBy,sortDir,startDate,endDate,month);
    }

    @PostMapping(ApiRoute.GENERATE_SPECIAL_WIN_REPORT)
    public ResponseEntity<?> generateSpecialWinReport (
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
            @RequestParam(value = "startDate", defaultValue = "1980-01-01T00:00:00")String startDate,
            @RequestParam(value = "endDate", defaultValue = "3000-01-01T00:00:00")String endDate,
            @RequestParam(value = "month", required = false) String month) {
        return crReportService.generateSpecialWinReport(pageNo,pageSize,sortBy,sortDir,startDate,endDate,month);
    }
}
