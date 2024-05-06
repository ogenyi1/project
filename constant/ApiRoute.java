package ng.optisoft.rosabon.constant;

public interface ApiRoute {

    String SEPARATOR = "/";

    String DEFAULT_REQUEST_MAPPING = SEPARATOR + "api";

    String INITIALIZE_PAYMENT = "initialize-payment";

    String INITIALIZE_LOAN_REPAYMENT = "initialize-loan-repayment";

    String GET_FINAN_INSTITUTION = "/get-all-institutions";

    String CONNECT_MONO_SESSION = "/connect-session";

    String LEASE = "/leases";
    String PRODUCT = "/credit-product";
    String ETIX = "/etix";

    String GET_COMPANY_INCL = "/get-all-company-incl";

    String EXISTING_CASHBACKED = "/existing_cash_backed_loan";

    String REMOVE_ETIX = "/remove-etix";

    String REMOVE_CREDIT_PRODUCT = "/remove-credit-product";

    String GROUP_COMPANY = "/group-company";
    String INTEREST_TYPE = "/interest_type";
    String CREATE_REPAYMENT_TYPE = "/create-repayment-type";
//	String PROF_OCCUPATION = "/prof_occupation";

    String LOAN_REASON = "/loan-reason";

    String REMOVE_LOAN_REASON = "/remove-loan-reason";

    String PENAL_CHARGE = "/penal-charge";

    String PERFECTION = "/perfection";

    String REMOVE_PERFECTION = "/remove-perfection";
    String REMOVE_PENAL_CHARGE = "/remove-penal-charge";

    String PRODUCT_CATEGORY = "/product-category";
    String PRODUCT_TYPE = "/product-type";

    String REGISTRATION_MGT = "/registration-mgt";

    String REPAYMENT_TYPE = "/repayment-type";
    String TARGET_MARKET = "/target-market";
    String TOP_UP = "/top-up";

    String REQUIREMENT = "/requirement";

    String VEHICLE_MGT = "/vehicle-mgt";

    String UPLOAD_LOAN_REQUIREMENT = "/upload-loan-requirement";

    String ERP_PRODUCT_DOCUMENT_CHECKLIST = "/document_checklist";


    String MAKE_BANK_STATEMENT_CALL = "/request-bank-statement";

    String GET_ALL_CREDIT_PRODUCT = "/get-all-credit-product";

    String SUBMIT_LOAN_PROCESSING = "/submit-loan-processing";

    String COMPLETE_REPAYMENT_SETUP = "/complete-loan-repayment-setup";

    String GENERATE_REPAYMENT = "/generate-repayment";

    String INITIALIZE_LOAN = "/initialize-loan-processing";

    String ACCEPT_LOAN_OFFER = "/accept-loan-offer";

    String GET_LOAN_VALUE = "/get-loan-value";

    String GET_PENAL = "/get-all-penal";

    String GET_ALL_VEHICLE = "/get-all-vehicle-registration";

    String GET_OCCUPATION = "/get-all-professional-occupation";

    String GET_LIQUIDATION = "/get-all-liquidation";
    String GET_ETIX = "/get-all-etix";
    String GET_PERFECTION = "/get-all-perfection";
    String GET_REPAYMENT_TYPE = "/get-all-repayment-type";

    String GET_GROUP_COMPANY = "/get-all-group-company";

    String GET_INDUSTRY_MANAGEMENT = "/get-all-industry-management";

    String GET_ALL_REGISTRATION = "/get-all-registration";

    String GET_ALL_TOP_UP = "/get-all-top-up";

    String GET_BY_ADMIN_REF_CODE = "/get-all-users-under-admin";

    String AUTH = "/auth";

    String ADMIN = "/admin";

    String LOGIN = "/login";
    String LOGOUT = "/logout";


    String FACEBOOK_LOGIN = "/facebook-login";

    String GOOGLE_LOGIN = "/google-login";

    String REGISTER = "/register";

    String ACTUATOR = "/actuator";

    String PATIENTS = "/patients";

    String COUNTRY = "/country";

    String STATE = "/state";

    String LGA = "/lga";

    String USERS = "/users";

    String USER = "/user";

    String CHANGE_PASSWORD = "/change-password";

    String FORGOT_PASSWORD = "/forgot-password";
    String RESET_PASSWORD = "/reset-password";

    String RESEND_EMAIL_VERIFICATION_CODE = "/resend-email-verification-code";

    String VERIFY_EMAIL = "/verify-email";

    String BY_PROPERTY_CREATOR = "/by-property-creator";

    String BY_PROPERTY_OWNER = "/by-property-owner";

    String NON_ADMIN_USERS = "/non-admin-users";

    String ADMIN_USERS = "/admin-users";

    String PROPERTIES = "/properties";

    String APPOINTMENT_BOOKING = "/appointment-booking";

    String IPO_STAKE = "/ipo-stake";

    String MERGE = "/merge";

    String QUICK_MERGE = "/quick-merge";

    String BID = "/bid";

    String BIDS = "/bids";

    String UNITS = "/units";

    String PROPERTIES_IPO_STAKE_PURCHASED = "/properties-ipo-stake-purchased";

    String BIDS_ON_USER_IPOSTAKES = "/bids-on-user-ipostakes";

    String BIDS_BY_USER = "/bids-by-user";

    String BID_COUNTER_COST = "/bid-counter-cost";

    String COUNTER_A_BID = "/counter-a-bid";

    String BID_UPDATING_COST = "/bid-updating-cost";

    String UPDATE_A_BID = "/update-a-bid";

    String ACCEPT_A_BID = "/accept-a-bid";

    String SPLIT = "/split";

    String PUT_UP_FOR_SALE = "/put-up-for-sale";

    String BUY = "/buy";

    String PAYMENT_VERIFICATION = "/payment-verification";

    String TRANSACTIONS = "/transactions";

    String RUNNING_TRAN = "running";

    String TEST_CARD_TRANSACTION = "/initialize-card-testing";

    String TEST_CASH_REFUND = "/refund";

    String PAYMEMNT_GATEWAYS = "/payment-gateways";

    String WALLET_BALANCE = "/wallet-balance";

    String CREDIT_WALLET_BALANCE = "/credit-wallet-balance";

    String WALLET_TRANSACTNS = "/wallet-transactions";

    String UNIT_COST = "/unit-cost";

    String ACTIVATE = "/activate";

    String NOTIFICATIONS = "/notifications";

    String BY_NAME = "/by-name";

    String UPDATE_AND_ACTIVATE = "/update-and-activate";

    String ADD_IMAGES = "/add-images";

    String REMOVE_IMAGE = "/remove-image";

    String ALL = "/all";

    String UPDATE_STATUS = "/update-status";

    String STATUS_LIST = "/status-list";

    String FUND_USER_WALLET = "/fund-user-wallet";

    String ENBALE_OR_DISABLE = "/enable-or-disable";

    String BANKS = "/banks";

    String BANK_ACCOUNT = "/bank-account";

    String FUND_BANK_ACCOUNT = "/fund-bank-account";

    String PAYSTACK = "/paystack";

    String MONO_WEBHOOK = "/mono";

    String GET_WEB_HOOK_NOTIFICATION = "/get-web-hook-notification";

    String GET_MONO_WEB_HOOK_NOTIFICATION = "/get-mono-web-hook-notification";

    String BANK_TRANSFER_VERIFICATION = "/bank-transfer-verification";

    String AUDIT_TRAIL = "/audit-trail";

    String PRICE = "/price";

    String EMAIL = "/email";

    String CONFIG_DATA = "/config-data";

    String DASHBOARD_STATS = "/dashboard-stats";

    // Credit
    String ADD_TARGET_MARKET = "/add-target-market";

    String ADD_PRODUCT_CATEGORY = "/add-product-category";

    String ADD_PRODUCT_TYPE = "/add-product-type";

    String ADD_REQUIREMENT = "/add-product-requirement";

    String ADD_TOP_UP = "/add-top-up";

    String ADD_VEHICLE = "/add-vehicle";

    String ADD_INDUSTRY = "/add-industry";

    String ADD_INTEREST_TYPE = "/add-interest-type";

    String GET_INTEREST_TYPE = "/get-all-interest-type";

    String ADD_REPAYMENT_TYPE = "/add-repayment-type";

    String ADD_GROUP_COMPANY = "/add-group-company";


    String PROF_OCCUPATION = "/add-prof-occupation";

    String ADD_PROF_OCCUPATION = "/add-prof-occupation";


    String REGISTER_VEHICLE = "/reg-vehicle";

    String ADD_LIQUIDATION = "/add-liquidation";

    String ADD_PENAL_CHARGE = "/add-penal-charge";

    String ADD_COMPANY_INCLUSION = "/add-company-inclusion";

    String ADD_PERFECTION = "/add-perfection";

    String ADD_ETIX = "/add-etix";

    String ADD_BONUS_SET_UP = "/set-bonus";

    String ADD_PRODUCT = "/add-credit-product";

    String UPLOAD_BONUS_SCHEDULE = "/upload-bonus-schedule";

    String UPDATE_ETIX = "/update-etix";

    String UPDATE_PERFECTION = "/update-perfection";

    String UPDATE_COMPANY_INCLUSION = "/update-company-inclusion";

    String UPDATE_PENAL_CHARGE = "/update-penal-charge";

    String UPDATE_LIQUIDATION = "/update-liquidation";

    String UPDATE_REGISTER_VEHICLE = "/update-vehicle-reg";

    String UPDATE_REPAYMENT_TYPE = "/update-repayment-type";

    String UPDATE_INTEREST_TYPE = "/update-interest-type";

    String UPDATE_GROUP_COMPANY = "/update-group-company";

    String UPDATE_REQUIREMENT = "/update-requirement";

    String UPDATE_TOP_UP = "/update-top_up";

    String UPDATE_VEHICLE = "/update-vehicle";

    String UPDATE_INDUSTRY = "/update-industry";

    String UPDATE_PROF_OCCUPATION = "/update-pro-occupation";


    String REMOVE_TARGET_MARKET = "/remove-target-market";

    String REMOVE_PRODUCT_CATEGORY = "/remove-product-category";

    String REMOVE_PRODUCT_TYPE = "/remove-product-type";

    String REMOVE_PRODUCT_REQUIREMENT = "/remove-product-requirement";

    String REMOVE_TOP_UP = "/remove-top-up";

    String REMOVE_INTEREST_TYPE = "/remove-interest-type";

    String REMOVE_REPAYMENT_TYPE = "/remove-repayment-type";

    String REMOVE_GROUP_COMPANY = "/remove-group-company";

    String REMOVE_VEHICLE = "/remove-vehicle";

    String REMOVE_INDUSTRY = "/remove-industry";


    String FEEDBACK = "/feedback";

    String CONFIRM_EMAIL = "/confirm";

    String OPEN_TICKETS = "/open-tickets";

    String CLOSED_TICKETS = "/closed-tickets";

    String MY_TICKETS = "/my-tickets";

    String EDIT_TICKET = "/edit-ticket";

    String CREATE_CATEGORY = "/create-category";

    String CLOSE_TICKET = "/close-ticket";

    String REOPEN_TICKET = "/reopen-ticket";

    String ARCHIVED_TICKETS = "/archived-tickets";

    String CATEGORIES = "/categories";

    String EDIT_CATEGORY = "/edit-category";

    String REPLY_TICKET = "/reply-ticket";


    //GET
    String GET_INCLUSION = "/get-company-incl-details";

    String COMPANY = "/company";

    String EDIT_COMPANY = "/edit-company";

    String INDIVIDUAL_USER = "/individual-user";

    String GET_ALL_BANKS = "/get-all-banks";

    String VERIFY = "/verify";

    String EDIT_DOCUMENT = "/edit-document";

    String MY_DOCUMENT = "/my-document";

    String GET_ALL_BONUS_SCHEDULE = "/get-bonus-schedules";

    String GET_TARGET_MARKET = "/get-all-target-market";

    String GET_PRODUCT_CODES = "/get-all-product-codes";

    String GET_ERP_PRODUCTS = "/get-all-erp-products";

    String GET_ERP_EMPLOYERS = "/get-all-erp-employers";

    String GET_ERP_EMPLOYERS_INDUSTRY = "/get-all-erp-employers-industry";

    String GET_ERP_SECTOR = "/get-all-erp-sectors";

    String USER_TYPES = "/get-user-types";

    String BUSINESS_TYPE = "/get-business-types";

    String GET_LOAN_REQUIREMENT = "/get-all-loan-requirement";
    String GET_PRODUCT_CATEGORY = "/get-all-product-category";

    String GET_PRODUCT_TYPE = "/get-all-product-type";

    String SEND_OTP = "/send-otp";

    String DIRECTOR_DETAILS = "/director-details";

    String COMPANY_DOCUMENT = "/company-document";

    String VERIFY_BVN = "/verify-bvn";

    String UPDATE_PRODUCT = "/update-product";

    String ADMIN_PATH = "admin";
    String PROVIDUS_PATH = SEPARATOR + "providus";

    String PROVIDUS_CREATE_DYNAMIC_ACCOUNT = SEPARATOR + "create-dynamic-account";

    String PROVIDUS_CREATE_DYNAMIC_ACCOUNT_NUMBER_URL = SEPARATOR + " ";
    String PROVIDUS_UPDATE_ACCOUNT_NAME_URL = SEPARATOR + "update-name";
    String PROVIDUS_VERIFY_TRANSACTION_URL = SEPARATOR + "verify-transaction";
    String PROVIDUS_VERIFY_TRANSACTION_BY_SESSION_ID_URL = SEPARATOR + "verify-transaction-by-sessionid";
    String PROVIDUS_VERIFY_TRANSACTION_BY_SETTLEMENT_ID_URL = SEPARATOR + "verify-transaction-by-settlementid";
    String PROVIDUS_BLACKLIST_ACCOUNT_URL = SEPARATOR + "blacklist-account";
    String PROVIDUS_CREATE_RESERVED_ACCOUNT_NUMBER_URL = SEPARATOR + "create-reserved-account";
    String PROVIDUS_SETTLEMENT_NOTIFICATION_URL = SEPARATOR + "settlement_notif";
    String PROVIDUS_SETTLEMENT_REPUSH_URL = SEPARATOR + "providus_repush";

    // Treasury

    String TR_PRODUCT_CATEGORY = "/trproduct-category";
    String TR_CREATE_PLAN = "/trcreate-plan";

    String CREDIT_WALLET = "/credit=wwallet";
    String TR_PLAN_ACTION = "/trplan-action";
    String TR_CONTRIBUTION_VALUE = "/trcontrib-value";
    String TR_CURRENCY = "/trcurrency";
    String TR_EXCHANGE = "/trexchange-rates";
    String TR_INVESTMENT = "/trinvestment-rates";
    String TR_PENAL = "/trpenal-charge";

    String TR_PRODUCT = "/trproduct";
    String TR_PROPERTY = "/trproperty";
    String PROCESS = "/process-flow";
    String STAGE = "/stage";
    String TR_WITHHOLDING_TAX = "/trwithholding-tax";
    String TR_TENOR = "/trtenor";
    String DATE_INFO = "/date-info";
    String TR_WITHDRAWAL = "/trwithdrawal";


    String UPDATE_PRODUCT_TYPE = "/update-product-type";

    String UPDATE_PRODUCT_CATEGORY = "/update-product-category";

    String UPDATE_TARGET_MARKET = "/update-target-market";

    String SOURCES = "/sources";


    String GET_LOAN_REASON = "/get-loan-reasons";
    String ACCESS_LOAN_PRODUCT = "/access-loan-product";

    String ADD_LOAN_REASON = "/add-loan-reason";

    String UPDATE_LOAN_REASON = "/update-loan-reason";


    String BRANCHES = "/branches";

    String DEPARTMENTS = "/departments";

    String ROLE = "/role";

    String ASSIGN_ROLE = "/assign-role";

    String VERIFY_PHONE = "/verify-phone";

    String CONTACT_DETAIL = "/contact-detail";

    String VALIDATE_PHONE = "/validate-phone";

    String GET_BY_ID = "/get-by-id";

    String VALIDATE_OTP = "/validate-otp";

    String MODULE = "/module";

    String MODULE_ITEM = "/module-item";

    String STUDENT_DETAILS = "/student-details";

    String GET_BY_USER = "/get-by-user";

    String NOK_DETAIL = "/nok-detail";

    String CREDIT = "/credit";

    String TICKET_REPLY = "/ticket-reply";

    String CUSTOMER_DATABOARD = "/customer-databoard";

    String PERSONAL_INFO = "/personal-info";

    String EMPLOYMENT_DETAIL = "/employment-detail";

    String CUSTOMER_DETAILS = "/customer-details";

    String BUSINESS_DETAILS = "/business-details";

    String CONTACT_PERSON_DETAILS = "/contact-person-details";

    String OCCUPATION_DETAILS = "/occupation-details";

    String WALLETS = "/wallets";

    String WITHDRAW_TO_BANK = "/withdraw-to-bank";


    String WEB_HOOK = "/web-hook";

    String GET_TRANSFER_STATUS = "/get-transfer-status";
    String AUTH_TRANSFER = "/authenticate-transfer";
    String REQUEST_WITHDRAWAL = "/request-withdrawal";
    String UPDATE_AFTER_REQUEST_APPROVAL = "/update-after-request-approval";
    String ACCEPT_OR_REJECT_WITHDRAWAL_REQUEST = "/accept-or-reject-withdrawal-request";

    String REMOVE_REGISTRATION = "/remove-regisration-mgt";

    String REMOVE_PROF_OCCU = "/remove-prof-occupation";

    String GENDER = "/gender";

    String IDENTIFICATION_TYPE = "/identification-type";


    String TR_PLAN_HISTORY = "/tr-plan-history";
    String FAQ = "/faq";
    String MESSAGE_CENTRE = "/message-centre";
    String FAQ_CATEGORY = "/faq-category";

    String WALLET_TRANSFER = "/wallet-transfer";

    String ELIGIBLE_PLANS_FOR_TRANSFER = "/eligible-plans-for-transfer";

    String HISTORY = "/history";

    String VIRTUAL_ACCOUNT = "/virtual-account";

    String REFERRALS = "/referrals";

    String POKE = "/poke";

    String REDEEM_BONUS = "/redeem-bonus";

    String ACTIVITIES = "/activities";

    String MY_DEPOSITS = "/my-deposits";

    String GET_TOTAL_WALLET_BALANCE = "/get-total-wallet-balance";

    String GET_CUSTOMER_WALLET_BALANCE = "/get-customer-wallet-balance";

    String SET_ACTIVATION_REFERRAL_BONUS = "/set-activation-referral-bonus";

    String SET_REFERRAL_REDEEM_THRESHOLD = "/set-referral-redeem-threshold";

    String GET_REFERRAL_REDEEM_THRESHOLD = "/get-referral-redeem-threshold";

    String GET_ACTIVATION_REFERRAL_BONUS = "/get-activation-referral-bonus";

    String GET_REACTIVATION_REFERRAL_BONUS = "/get-reactivation-referral-bonus";

    String SET_REACTIVATION_REFERRAL_BONUS = "/set-reactivation-referral-bonus";

    String REQUEST_CENTRE = "/request-centre";

    String CREATE_CUSTOMER_REQUEST = "/create-customer-request";

    String UPDATE_BVN = "/update-bvn";

    String PENDING_REQUESTS = "/pending-requests";

    String SPECIAL_EARNINGS = "/special-earnings";

    String CUSTOMER_INVESTMENT = "/customer-investment";

    String INITIATE_TOPUP = "/initiate-topup";

    String TRANSFER = "/transfer";

    String WITHDRAW = "/withdraw";

    String MY_QUEUE = "/my-queue";

    String TREAT_REQUEST = "/treat";

    String REPORTS = "/reports";

    String GENERATE_TRANSACTION_REPORT = "/generate-transaction-report";
    String GENERATE_WALLET_TRANSACTION_REPORT = "/generate-wallet-transaction-report";

    String GET_KYC_DETAILS = "/get-kyc-details";

    String GENERATE_INVESTMENT_REPORT = "/generate-investment-report";

//    String GET_KYC_DETAILS = "get-kyc-details";

    String ACCOUNT_OFFICER = "/account-officer";
    String GENERATE_REFERRAL_REPORT = "/generate-referral-report";

    String BONUS = "/bonus";

    String IS_REDEEMABLE = "/is-redeemable";

    String UNLOCK_USER = "/unlock-user";

    String RIGHTS = "/rights";

    String ELIGIBLE_MODULES = "/eligible-modules";

    String UPLOAD_SCHEDULE_PAYOUT = "/upload-schedule-payout";

    String PAYOUT_ACTIVITIES = "/payout-activities";

    String TOTAL_REDEEMED_EARNING = "/total-redeemed-earning";

    String TOTAL_EARNING = "/total-earning";

    String UPDATE_FIRST_LAST_NAME = "/update-first-last-name";

    String MAKE_REPAYMENT = "/make-repayment";

    String DEBIT_CARD = "/debit-card";

    String CREATE = "/create";

    String USSD_CODES = "/ussd-codes";

    String UPDATE = "/update";

    String DELETE = "/delete";

    String MY_REFERRAL_BONUS_AMOUNT_CREDIT = "/my-referral-bonus-amount-credit";

    String MY_REFERRAL_BONUS_CREDIT = "/my-referral-bonus-credit";

    String MY_REFERRAL_BONUS_CREDIT_REDEEM_THRESHOLD = "/my-referral-bonus-credit-redeem-threshold";

    String ROSABON_SPECIAL_EARNINGS = "/rosabon-special-earnings";

    String GIFT = "/gift";

    String ELIGIBLE_MODULE_ITEMS = "/eligible-module-items";

    String FUND_USER_TREASURY_WALLET = "/fund-user-treasury-wallet";

    String FIND_BY_PARAM = "/find-by-param";

    String GENERATE_CUSTOMER_REPORT = "/generate-customer-report";

    String GENERATE_DISBURSEMENT_REPORT = "/generate-disbursement-report";

    String GENERATE_PORTFOLIO_REPORT = "/generate-portfolio-report";

    String GENERATE_REFERRAL_BONUS_REPORT_FOR_ACTIVATION = "/generate-referral-bonus-report-for-activation";

    String GENERATE_REFERRAL_BONUS_REPORT_FOR_REACTIVATION = "/generate-referral-bonus-report-for-reactivation";

    String GENERATE_SPECIAL_WIN_REPORT = "/generate-special-win-report";

    String RESET_PASSWORD2 = "/reset-password2";

    String BANK_ACCOUNT_BUSINESS = "bank-account-business";

    String IS_POKEABLE = "/is-pokeable";

    String ERP = SEPARATOR + "erp";
    String V1 = SEPARATOR + "v1";
    String WEMA_VAS = SEPARATOR + "wema-vas";
    String WEBHOOKS = SEPARATOR + "webhooks";
    String TREASURY = SEPARATOR + "treasury";
    String WEMA_GET_TOKEN_PATH = SEPARATOR + "Authentication/authenticate";
    String WEMA_GET_BALANCE_PATH = SEPARATOR + "WMServices/GetBalance?AccountNumber=";

    String WEMA_NIP_FUND_TRANSFER_PATH = SEPARATOR + "WMServices/NIPFundTransfer";
    String WITHDRAWAL_THRESHOLDS = SEPARATOR + "withdrawal-thresholds";

    String VENDORS = "/vendors";
    String LEASE_BRAND = "/lease-brands";
    String LEASE_TYPES = "/lease_types";
    String LEASE_PRODUCT_TYPE = "/lease_product_type";
    String LEASE_STATUS = "/lease_status";

    String USSD = SEPARATOR + "ussd";
    String ROSABON_WIN_BIG_NOTE = SEPARATOR + "rosabon-win-big-note";
}
