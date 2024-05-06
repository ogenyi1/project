package ng.optisoft.rosabon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    private final ReportService reportService;

    @PreAuthorize("hasAnyAuthority('VIEW_REPORT', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.REPORTS + ApiRoute.GENERATE_TRANSACTION_REPORT)
    public ResponseEntity<?> generateTransactionReport (Authentication auth,
                                                        @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                        @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                                        @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
                                                        @RequestParam(value = "customerId", defaultValue = "000000000") String customerId,
                                                        @RequestParam(value = "transactionType", defaultValue = "all") String transactionType,
                                                        @RequestParam(value = "customerIdStart", defaultValue = "000000000") String customerIdStart,
                                                        @RequestParam(value = "customerIdEnd", defaultValue = "999999999") String customerIdEnd,
                                                        @RequestParam(value = "startDate", defaultValue = "01-01-1980")String startDate,
                                                        @RequestParam(value = "endDate", defaultValue = "01-01-3000")String endDate,
                                                        @RequestParam(name="admin-usage", required = false) Useraccount.Usage adminUsage) {
        return reportService.generateTransactionReport2(
                auth,
                pageNo,
                pageSize,
                sortBy,
                sortDir,
                customerId,
                transactionType,
                customerIdStart,
                customerIdEnd,
                startDate,
                endDate,
                adminUsage
        );
    }

    @PreAuthorize("hasAnyAuthority('VIEW_REPORT', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.REPORTS + ApiRoute.GENERATE_WALLET_TRANSACTION_REPORT)
    public ResponseEntity<?> generateWalletTransactionReport (Authentication auth,
                                                        @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                        @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                                        @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
                                                        @RequestParam(value = "customerId", defaultValue = "000000000") String customerId,
                                                        @RequestParam(value = "transactionType", defaultValue = "all") String transactionType,
                                                        @RequestParam(value = "customerIdStart", defaultValue = "000000000") String customerIdStart,
                                                        @RequestParam(value = "customerIdEnd", defaultValue = "999999999") String customerIdEnd,
                                                        @RequestParam(value = "startDate", defaultValue = "01-01-1980")String startDate,
                                                        @RequestParam(value = "endDate", defaultValue = "01-01-3000")String endDate,
                                                        @RequestParam(name="admin-usage", required = false) Useraccount.Usage adminUsage) {
        return reportService.generateWalletTransactionReport(
                auth,
                pageNo,
                pageSize,
                sortBy,
                sortDir,
                customerId,
                transactionType,
                customerIdStart,
                customerIdEnd,
                startDate,
                endDate,
                adminUsage
        );
    }

    @PreAuthorize("hasAnyAuthority('VIEW_REPORT', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value=ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.REPORTS + ApiRoute.GENERATE_INVESTMENT_REPORT)
    public ResponseEntity<?> generateInvestmentReport (Authentication auth,
                                                       @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                       @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                       @RequestParam(value = "sortBy", defaultValue = "created_date") String sortBy,
                                                       @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
                                                       @RequestParam(value = "customerId", defaultValue = "000000000") String customerId,
                                                       @RequestParam(value = "productType", defaultValue = "all") String productType,
                                                       @RequestParam(value = "customerType", defaultValue = "all") String customerType,
//                                                       @RequestParam(value = "customerIdStart", defaultValue = "000000000") String customerIdStart,
//                                                       @RequestParam(value = "customerIdEnd", defaultValue = "999999999") String customerIdEnd,
                                                       @RequestParam(value = "startDate", defaultValue = "01-01-1980")String startDate,
                                                       @RequestParam(value = "endDate", defaultValue ="01-01-3000")String endDate,
//                                                       @RequestParam(value = "startAmount", defaultValue = "0.00", required = false)String startAmount,
//                                                       @RequestParam(value = "endAmount", defaultValue = "17976931348623157.00", required = false)String endAmount,
                                                       @RequestParam(name="admin-usage", required = false) Useraccount.Usage adminUsage) {
        return reportService.generateInvestmentReport2(
                auth,
                pageNo,
                pageSize,
                sortBy,
                sortDir,
                customerId,
                productType,
//                customerIdStart,
//                customerIdEnd,
                customerType,
                startDate,
                endDate,
//                startAmount,
//                endAmount,
                adminUsage
        );
    }

    @PreAuthorize("hasAnyAuthority('VIEW_REPORT', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value=ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.REPORTS + ApiRoute.GENERATE_REFERRAL_REPORT)
    public ResponseEntity<?> generateReferralReport (Authentication auth,
                                                       @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                       @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                       @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                                       @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir,
                                                       @RequestParam(value = "referralCode", defaultValue = "000000000",required = false) String referralCode,
                                                       @RequestParam(value = "source", defaultValue = "all", required = false) String source,
                                                       @RequestParam(value = "startDate", defaultValue = "01-01-1980")String startDate,
                                                       @RequestParam(value = "endDate", defaultValue ="01-01-3000")String endDate,
                                                       @RequestParam(value = "startAmount", defaultValue = "0.00", required = false)String startAmount,
                                                       @RequestParam(value = "endAmount", defaultValue = "17976931348623157.00", required = false)String endAmount,
                                                       @RequestParam(name="admin-usage", required = false) Useraccount.Usage adminUsage) {
        return reportService.referralReport(
                auth,
                pageNo,
                pageSize,
                sortBy,
                sortDir,
                referralCode,
                source,
                startDate,
                endDate,
                startAmount,
                endAmount,
                adminUsage
        );
    }

    @PreAuthorize("hasAnyAuthority('VIEW_REPORT', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value=ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.REPORTS + ApiRoute.SEPARATOR + "/generate-wallet-portfolio-report")
    public ResponseEntity<?> generateWalletPortfolioReport (Authentication auth,
                                                            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                                            @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
                                                            @RequestParam(value = "customerId", defaultValue = "000000000") String customerId,
                                                            @RequestParam(value = "customerName", defaultValue = "all") String customerName,
                                                            @RequestParam(value = "customerType", defaultValue = "all") String customerType,
//                                                            @RequestParam(value = "customerIdStart", defaultValue = "000000000") String customerIdStart,
//                                                            @RequestParam(value = "customerIdEnd", defaultValue = "999999999") String customerIdEnd,
                                                            @RequestParam(value = "startDate", defaultValue = "01-01-1980")String startDate,
                                                            @RequestParam(value = "endDate", defaultValue ="01-01-3000")String endDate,
//                                                            @RequestParam(value = "startAmount", defaultValue = "0.00", required = false)String startAmount,
//                                                            @RequestParam(value = "endAmount", defaultValue = "17976931348623157.00", required = false)String endAmount,
                                                            @RequestParam(name="admin-usage", required = false) Useraccount.Usage adminUsage) {
        return reportService.generateWalletPortfolioReport(
                auth,
                pageNo,
                pageSize,
                sortBy,
                sortDir,
                customerId,
                customerName,
                customerType,
//                customerIdStart,
//                customerIdEnd,
                startDate,
                endDate,
//                startAmount,
//                endAmount,
                adminUsage
        );
    }

    @PreAuthorize("hasAnyAuthority('VIEW_REPORT', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value=ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.REPORTS + ApiRoute.SEPARATOR + "/generate-customer-source-report")
    public ResponseEntity<?> generateCustomerSourceReport (Authentication auth,
                                                            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                                            @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
                                                            @RequestParam(value = "customerId", defaultValue = "000000000") String customerId,
                                                            @RequestParam(value = "customerName", defaultValue = "all") String customerName,
                                                            @RequestParam(value = "companyName", defaultValue = "all") String companyName,
                                                            @RequestParam(value = "customerType", defaultValue = "all") String customerType,
                                                            @RequestParam(value = "registrationChannel", defaultValue = "all") String registrationChannel,
                                                            @RequestParam(value = "source", defaultValue = "all", required = false) String source,
                                                            @RequestParam(value = "referredBy", defaultValue = "all", required = false) String referredBy,
//                                                            @RequestParam(value = "investmentStatus", defaultValue = "all", required = false) String investmentStatus,
                                                            @RequestParam(value = "startDate", defaultValue = "01-01-1980")String startDate,
                                                            @RequestParam(value = "endDate", defaultValue ="01-01-3000")String endDate,
                                                            @RequestParam(name="admin-usage", required = false) Useraccount.Usage adminUsage) {
        return reportService.generateCustomerSourceReport(
                auth,
                pageNo,
                pageSize,
                sortBy,
                sortDir,
                customerId,
                customerName,
                companyName,
                customerType,
                registrationChannel,
                source,
                referredBy,
//                investmentStatus,
                startDate,
                endDate,
                adminUsage
        );
    }

    @PreAuthorize("hasAnyAuthority('VIEW_REPORT', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value=ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.REPORTS + ApiRoute.SEPARATOR + "/generate-customer-wallet-transaction-statement-report")
    public ResponseEntity<?> generateCustomerWalletTransactionStatementReport (Authentication auth,
                                                           @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                           @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                                           @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
                                                           @RequestParam(value = "customerId", defaultValue = "all") String customerId,
                                                           @RequestParam(value = "startDate", defaultValue = "01-01-1980")String startDate,
                                                           @RequestParam(value = "endDate", defaultValue ="01-01-3000")String endDate,
                                                           @RequestParam(name="admin-usage", required = false) Useraccount.Usage adminUsage) {
        return reportService.generateCustomerWalletTransactionStatementReport(
                auth,
                pageNo,
                pageSize,
                sortBy,
                sortDir,
                customerId,
                startDate,
                endDate,
                adminUsage
        );
    }

    @PreAuthorize("hasAnyAuthority('VIEW_REPORT', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value=ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.REPORTS + ApiRoute.SEPARATOR + "/generate-customer-registration-report")
    public ResponseEntity<?> generateCustomerRegistrationReport (Authentication auth,
                                                                 @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                                 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                                 @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                                                 @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
                                                                 @RequestParam(value = "startDate", defaultValue = "01-01-1980")String startDate,
                                                                 @RequestParam(value = "endDate", defaultValue ="01-01-3000")String endDate,
                                                                 @RequestParam(name="admin-usage", required = false) Useraccount.Usage adminUsage) {
        return reportService.generateCustomerRegistrationReport(
                auth,
                pageNo,
                pageSize,
                sortBy,
                sortDir,
                startDate,
                endDate,
                adminUsage
        );
    }

    @PreAuthorize("hasAnyAuthority('VIEW_REPORT', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value=ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.REPORTS + ApiRoute.SEPARATOR + "/generate-interest-expense-report")
    public ResponseEntity<?> generateInterestExpenseReport (Authentication auth,
                                                                 @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                                 @RequestParam(value = "pageSize", defaultValue = "30") int pageSize,
                                                                 @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                                                 @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
                                                                 @RequestParam(value = "startDate", defaultValue = "01-01-1980")String startDate,
                                                                 @RequestParam(value = "endDate", defaultValue ="01-01-3000")String endDate,
                                                                 @RequestParam(name="admin-usage", required = false) Useraccount.Usage adminUsage) {
        return reportService.generateInterestExpenseReport(
                auth,
                pageNo,
                pageSize,
                sortBy,
                sortDir,
                startDate,
                endDate,
                adminUsage
        );
    }

    @PreAuthorize("hasAnyAuthority('VIEW_REPORT', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value=ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.REPORTS + ApiRoute.SEPARATOR + "/generate-cbn-mom-investment-report")
    public ResponseEntity<?> generateCbnMomInvestmentReport (Authentication auth,
                                                            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                                            @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
                                                            @RequestParam(value = "startDate", defaultValue = "01-01-1980")String startDate,
                                                            @RequestParam(value = "endDate", defaultValue ="01-01-3000")String endDate,
                                                            @RequestParam(name="admin-usage", required = false) Useraccount.Usage adminUsage) {
        return reportService.generateCbnMomInvestmentReport(
                auth,
                pageNo,
                pageSize,
                sortBy,
                sortDir,
                startDate,
                endDate,
                adminUsage
        );
    }

    @PreAuthorize("hasAnyAuthority('VIEW_REPORT', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value=ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.REPORTS + ApiRoute.SEPARATOR + "generate-adhoc-investment-report")
    public ResponseEntity<?> generateAdhocInvestmentReport (Authentication auth,
                                                       @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                       @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                       @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                                       @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
                                                       @RequestParam(value = "customerId", defaultValue = "000000000") String customerId,
                                                       @RequestParam(value = "productType", defaultValue = "all") String productType,
                                                       @RequestParam(value = "customerType", defaultValue = "all") String customerType,
                                                       @RequestParam(value = "startDate", defaultValue = "01-01-1980")String startDate,
                                                       @RequestParam(value = "endDate", defaultValue ="01-01-3000")String endDate,
                                                       @RequestParam(name="admin-usage", required = false) Useraccount.Usage adminUsage) {
        return reportService.generateAdhocInvestmentReport(
                auth,
                pageNo,
                pageSize,
                sortBy,
                sortDir,
                customerId,
                productType,
                customerType,
                startDate,
                endDate,
                adminUsage
        );
    }

    @PreAuthorize("hasAnyAuthority('VIEW_REPORT', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.REPORTS + ApiRoute.GENERATE_TRANSACTION_REPORT + "/full")
    public ResponseEntity<?> generateTransactionReportFull (Authentication auth,
                                                        @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                                        @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
                                                        @RequestParam(value = "customerId", defaultValue = "000000000") String customerId,
                                                        @RequestParam(value = "transactionType", defaultValue = "all") String transactionType,
                                                        @RequestParam(value = "customerIdStart", defaultValue = "000000000") String customerIdStart,
                                                        @RequestParam(value = "customerIdEnd", defaultValue = "999999999") String customerIdEnd,
                                                        @RequestParam(value = "startDate", defaultValue = "01-01-1980")String startDate,
                                                        @RequestParam(value = "endDate", defaultValue = "01-01-3000")String endDate,
                                                        @RequestParam(name="admin-usage", required = false) Useraccount.Usage adminUsage) {
        return reportService.generateTransactionReportFull(
                auth,
                sortBy,
                sortDir,
                customerId,
                transactionType,
                customerIdStart,
                customerIdEnd,
                startDate,
                endDate,
                adminUsage
        );
    }

    @PreAuthorize("hasAnyAuthority('VIEW_REPORT', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.REPORTS + ApiRoute.GENERATE_WALLET_TRANSACTION_REPORT + "/full")
    public ResponseEntity<?> generateWalletTransactionReportFull (Authentication auth,
                                                        @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                                        @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
                                                        @RequestParam(value = "customerId", defaultValue = "000000000") String customerId,
                                                        @RequestParam(value = "transactionType", defaultValue = "all") String transactionType,
                                                        @RequestParam(value = "customerIdStart", defaultValue = "000000000") String customerIdStart,
                                                        @RequestParam(value = "customerIdEnd", defaultValue = "999999999") String customerIdEnd,
                                                        @RequestParam(value = "startDate", defaultValue = "01-01-1980")String startDate,
                                                        @RequestParam(value = "endDate", defaultValue = "01-01-3000")String endDate,
                                                        @RequestParam(name="admin-usage", required = false) Useraccount.Usage adminUsage) {
        return reportService.generateWalletTransactionReportFull(
                auth,
                sortBy,
                sortDir,
                customerId,
                transactionType,
                customerIdStart,
                customerIdEnd,
                startDate,
                endDate,
                adminUsage
        );
    }

    @PreAuthorize("hasAnyAuthority('VIEW_REPORT', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value=ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.REPORTS + ApiRoute.GENERATE_INVESTMENT_REPORT + "/full")
    public ResponseEntity<?> generateInvestmentReportFull (Authentication auth,
                                                       @RequestParam(value = "sortBy", defaultValue = "created_date") String sortBy,
                                                       @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
                                                       @RequestParam(value = "customerId", defaultValue = "000000000") String customerId,
                                                       @RequestParam(value = "productType", defaultValue = "all") String productType,
                                                       @RequestParam(value = "customerType", defaultValue = "all") String customerType,
                                                       @RequestParam(value = "startDate", defaultValue = "01-01-1980")String startDate,
                                                       @RequestParam(value = "endDate", defaultValue ="01-01-3000")String endDate,
                                                       @RequestParam(name="admin-usage", required = false) Useraccount.Usage adminUsage) {
        return reportService.generateInvestmentReportFull(
                auth,
                sortBy,
                sortDir,
                customerId,
                productType,
                customerType,
                startDate,
                endDate,
                adminUsage
        );
    }

    @PreAuthorize("hasAnyAuthority('VIEW_REPORT', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value=ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.REPORTS + ApiRoute.GENERATE_REFERRAL_REPORT + "/full")
    public ResponseEntity<?> generateReferralReportFull (Authentication auth,
                                                     @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                                     @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir,
                                                     @RequestParam(value = "referralCode", defaultValue = "000000000",required = false) String referralCode,
                                                     @RequestParam(value = "source", defaultValue = "all", required = false) String source,
                                                     @RequestParam(value = "startDate", defaultValue = "01-01-1980")String startDate,
                                                     @RequestParam(value = "endDate", defaultValue ="01-01-3000")String endDate,
                                                     @RequestParam(value = "startAmount", defaultValue = "0.00", required = false)String startAmount,
                                                     @RequestParam(value = "endAmount", defaultValue = "17976931348623157.00", required = false)String endAmount,
                                                     @RequestParam(name="admin-usage", required = false) Useraccount.Usage adminUsage) {
        return reportService.generateReferralReportFull(
                auth,
                sortBy,
                sortDir,
                referralCode,
                source,
                startDate,
                endDate,
                startAmount,
                endAmount,
                adminUsage
        );
    }

    @PreAuthorize("hasAnyAuthority('VIEW_REPORT', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value=ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.REPORTS + ApiRoute.SEPARATOR + "/generate-wallet-portfolio-report/full")
    public ResponseEntity<?> generateWalletPortfolioReportFull (Authentication auth,
                                                            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                                            @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
                                                            @RequestParam(value = "customerId", defaultValue = "000000000") String customerId,
                                                            @RequestParam(value = "customerName", defaultValue = "all") String customerName,
                                                            @RequestParam(value = "customerType", defaultValue = "all") String customerType,
                                                            @RequestParam(value = "startDate", defaultValue = "01-01-1980")String startDate,
                                                            @RequestParam(value = "endDate", defaultValue ="01-01-3000")String endDate,
                                                            @RequestParam(name="admin-usage", required = false) Useraccount.Usage adminUsage) {
        return reportService.generateWalletPortfolioReportFull(
                auth,
                sortBy,
                sortDir,
                customerId,
                customerName,
                customerType,
                startDate,
                endDate,
                adminUsage
        );
    }

    @PreAuthorize("hasAnyAuthority('VIEW_REPORT', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value=ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.REPORTS + ApiRoute.SEPARATOR + "/generate-customer-source-report/full")
    public ResponseEntity<?> generateCustomerSourceReportFull (Authentication auth,
                                                           @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                                           @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
                                                           @RequestParam(value = "customerId", defaultValue = "000000000") String customerId,
                                                           @RequestParam(value = "customerName", defaultValue = "all") String customerName,
                                                           @RequestParam(value = "companyName", defaultValue = "all") String companyName,
                                                           @RequestParam(value = "customerType", defaultValue = "all") String customerType,
                                                           @RequestParam(value = "registrationChannel", defaultValue = "all") String registrationChannel,
                                                           @RequestParam(value = "source", defaultValue = "all", required = false) String source,
                                                           @RequestParam(value = "referredBy", defaultValue = "all", required = false) String referredBy,
                                                           @RequestParam(value = "startDate", defaultValue = "01-01-1980")String startDate,
                                                           @RequestParam(value = "endDate", defaultValue ="01-01-3000")String endDate,
                                                           @RequestParam(name="admin-usage", required = false) Useraccount.Usage adminUsage) {
        return reportService.generateCustomerSourceReportFull(
                auth,
                sortBy,
                sortDir,
                customerId,
                customerName,
                companyName,
                customerType,
                registrationChannel,
                source,
                referredBy,
                startDate,
                endDate,
                adminUsage
        );
    }

    @PreAuthorize("hasAnyAuthority('VIEW_REPORT', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value=ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.REPORTS + ApiRoute.SEPARATOR + "/generate-customer-wallet-transaction-statement-report/full")
    public ResponseEntity<?> generateCustomerWalletTransactionStatementReportFull (Authentication auth,
                                                                               @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                                                               @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
                                                                               @RequestParam(value = "customerId", defaultValue = "000000000") String customerId,
                                                                               @RequestParam(value = "startDate", defaultValue = "01-01-1980")String startDate,
                                                                               @RequestParam(value = "endDate", defaultValue ="01-01-3000")String endDate,
                                                                               @RequestParam(name="admin-usage", required = false) Useraccount.Usage adminUsage) {
        return reportService.generateCustomerWalletTransactionStatementReportFull(
                auth,
                sortBy,
                sortDir,
                customerId,
                startDate,
                endDate,
                adminUsage
        );
    }

    @PreAuthorize("hasAnyAuthority('VIEW_REPORT', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value=ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.REPORTS + ApiRoute.SEPARATOR + "/generate-customer-registration-report/full")
    public ResponseEntity<?> generateCustomerRegistrationReportFull (Authentication auth,
                                                                 @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                                                 @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
                                                                 @RequestParam(value = "startDate", defaultValue = "01-01-1980")String startDate,
                                                                 @RequestParam(value = "endDate", defaultValue ="01-01-3000")String endDate,
                                                                 @RequestParam(name="admin-usage", required = false) Useraccount.Usage adminUsage) {
        return reportService.generateCustomerRegistrationReportFull(
                auth,
                sortBy,
                sortDir,
                startDate,
                endDate,
                adminUsage
        );
    }

    @PreAuthorize("hasAnyAuthority('VIEW_REPORT', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value=ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.REPORTS + ApiRoute.SEPARATOR + "/generate-interest-expense-report/full")
    public ResponseEntity<?> generateInterestExpenseReportFull (Authentication auth,
                                                            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                                            @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
                                                            @RequestParam(value = "startDate", defaultValue = "01-01-1980")String startDate,
                                                            @RequestParam(value = "endDate", defaultValue ="01-01-3000")String endDate,
                                                            @RequestParam(name="admin-usage", required = false) Useraccount.Usage adminUsage) {
        return reportService.generateInterestExpenseReportFull(
                auth,
                sortBy,
                sortDir,
                startDate,
                endDate,
                adminUsage
        );
    }

    @PreAuthorize("hasAnyAuthority('VIEW_REPORT', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value=ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.REPORTS + ApiRoute.SEPARATOR + "/generate-cbn-mom-investment-report/full")
    public ResponseEntity<?> generateCbnMomInvestmentReportFull (Authentication auth,
                                                             @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                                             @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
                                                             @RequestParam(value = "startDate", defaultValue = "01-01-1980")String startDate,
                                                             @RequestParam(value = "endDate", defaultValue ="01-01-3000")String endDate,
                                                             @RequestParam(name="admin-usage", required = false) Useraccount.Usage adminUsage) {
        return reportService.generateCbnMomInvestmentReportFull(
                auth,
                sortBy,
                sortDir,
                startDate,
                endDate,
                adminUsage
        );
    }

    @PreAuthorize("hasAnyAuthority('VIEW_REPORT', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value=ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.REPORTS + ApiRoute.SEPARATOR + "generate-adhoc-investment-report/full")
    public ResponseEntity<?> generateAdhocInvestmentReportFull (Authentication auth,
                                                            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                                            @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
                                                            @RequestParam(value = "customerId", defaultValue = "000000000") String customerId,
                                                            @RequestParam(value = "productType", defaultValue = "all") String productType,
                                                            @RequestParam(value = "customerType", defaultValue = "all") String customerType,
                                                            @RequestParam(value = "startDate", defaultValue = "01-01-1980")String startDate,
                                                            @RequestParam(value = "endDate", defaultValue ="01-01-3000")String endDate,
                                                            @RequestParam(name="admin-usage", required = false) Useraccount.Usage adminUsage) {
        return reportService.generateAdhocInvestmentReportFull(
                auth,
                sortBy,
                sortDir,
                customerId,
                productType,
                customerType,
                startDate,
                endDate,
                adminUsage
        );
    }
}
