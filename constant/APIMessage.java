package ng.optisoft.rosabon.constant;

public interface APIMessage {

    String GET_LOAN_VALUE = "Get Loan value";

    String GET_LOAN_VALUE_ERROR = "Error while retrieving bank statement, please try again later";

    String ACCEPT_LOAN_OFFER = "Accept loan offer";

    String START_LOAN_PROCESSING = "Loan processing initialized";

    String START_LOAN_PROCESSING_ERROR = "Loan processing initializer failed, please try again";

    String GENERATE_REPAYMENT = "Generate repayment";

    String GENERATE_REPAYMENT_ERROR = "Generate repayment failed";

    String EXTRACT_BANK_STATEMENT = "Extract bank statement";

    String EXTRACT_BANK_STATEMENT_ERROR = "Extracting bank statement failed";

    String COMPLETE_REPAYMENT = "Complete Payment repayment";

    String COMPLETE_REPAYMENT_ERROR = "Failure completing payment repayment process";

    String UPLOAD_REQUIREMENT = "Upload loan requirement";

    String UPLOAD_REQUIREMENT_ERROR = "Failure uploading loan requirement";


    String SUBMIT_LOAN_PROCESSING = "Submit loan for processing";

    String SUBMIT_LOAN_PROCESSING_ERROR = "Error while submiting loan for processing";


    String USER_GET_CASH_BACKED_INFO = "Current user cash backed info";

    String SUCCESSFUL_REQUEST = "Your request was successful";
    String UNSUCCESSFUL_REQUEST = "Your request was not successful";
    String RECORD_NOT_FOUND = "Resource not found";
    String EMPTY_RESULT = "Resource not found";
    String USER_REGISTERED_SUCCESSFULLY = "User registered successfully";

    String REQUIRED_FULLNAME = "Name is requird";

    String REQUIRED_BVN = "Bvn is required";

    String REQUIRED_USERNAME = "Username is requird";

    String REQUIRED_EMAIL = "Email is required";

    String REQUIRED_PHONE = "Phone number is required";

    String REQUIRED_PASSWORD = "Password is required";
    String INVALID_TOKEN = "token is invalid";
    String WEMA_SUCCESS_STATUS = "00";
    String WEMA_SUCCESS_STATUS_DESC = "Okay";
    String WEMA_INVALID_ACCOUNT_STATUS = "07";
    String WEMA_INVALID_ACCOUNT_STATUS_DESC = "Invalid Account";
    String WEMA_DUPLICATE_RECORD_STATUS = "26";
    String WEMA_DUPLICATE_RECORD_STATUS_DESC = "Duplicate record";
    String WEMA_INVALID_TRANSACTION_STATUS = "26";
    String WEMA_INVALID_TRANSACTION_STATUS_DESC = "Invalid transaction";
    String WEMA_ACCOUNT_BLOCKED = "Account Restricted Successfully";
    String WEMA_ACCOUNT_NOT_BLOCKED = "Account Not Restricted Successfully";
    String INFLOW_TO_WALLET = "Great news! Your wallet has been successfully topped up with N{0}<br><br>There is a lot you can do with your funds. Whether you want to create a new investment or top-up your investments with the funds in your wallet, we are here to ensure you are well-equipped to achieve financial freedom.<br><br>If you have any questions or need assistance along the way, our dedicated support team is ready to assist you. Kindly reach out to us by sending a mail to clientservices@rosabon-finance.com or call us on 0700ROSABON (07007672266).<br>";
    String CONFLICT = "{0} already exists!";
    String NOT_FOUND = "{0} resource not found";
    String FAILED = "Request failed";
    String SUCCESS = "Request successful";
    String WEMA_NIP_FUND_TRANSFER_ORIGINATOR_NAME = "ROSABON FINANCIAL SERVICES";
}
