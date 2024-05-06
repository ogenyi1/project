package ng.optisoft.rosabon.constant;

public interface MessageConstant {

    String INACTIVE_ACCOUNT = "User account is Inactive, please contact admin for account activation";

    String WRONG_PLATFORM = "Wrong platform";
    String WRONG_PLATFORM_TYPE = "Wrong platform type";

    String PLATFORM_EMPTY = "Platform must be provided";
    String PLATFORM_TYPE_EMPTY = "Platform Type must be provided";

    String PLATFORM_FORBIDDEN = "You cannot access this platform type";

    String EMAIL_NOT_FOUND = "Email must be provided";

    String PASSWORD_NOT_FOUND = "Password must be provided";

    String INVALID_EMAIL = "Invalid email address provided";

    // String INVALID_CREDENTIALS = "Invalid credentials provided";
    String INVALID_CREDENTIALS = "Incorrect email and password combination. Please click on forgot password link to reset your password";

    String INVALID_USERNAME = "Invalid username provided";

    String PROVIDE_PAYMENT_ITEM_NAME = "Please provide the name of the payment item";

    String PROVIDE_PAYMENT_ITEM_DESCRIPTION = "Please provide the description of the payment item";

    String PROVIDE_PAYMENT_TYPE = "Please provide the payment type";

    String PROVIDE_PAYMENT_STATUS = "Please provide the payment status";

    String PROVIDE_PAYMENT_CODE = "Please provide the payment item code";

    String PROVIDE_PAYMENT_CATEGORY = "Please provide the payment category id";

    String PROVIDE_PAYMENT_ITEM_ID = "Please provide the payment item id";

    String INVALID_PAYMENTCATEGORY_ID = "Invalid payment category";

    String NO_AMOUNT_PROVIDED = "Amount must be provided for this type of payment item";

    String NO_CATEGORY_NAME = "A payment category name must be provided";

    String NO_CATEGORY_DESCRP = "A payment category description must be provided";

    String PAYMENT_CATEGORY_EXISTS = "A payment category with this name already exists";

    String NO_PAYMENT_PROPERTY_NAME = "A payment property name must be provided";

    String INVALID_PAYMENT_TYPE = "Please provide a valid payment type";

    String INVALID_PAYMENT_STATUS = "Please provide a valid payment status";

    String INVALID_PAYMENT_ITEM_ID = "Please provide a valid payment item id";

    String INVALID_PAYMENT_AMOUNT = "Please provide a valid payment amount";

    String PAYMENT_CATEGORY_NOT_FOUND = "Payment category not found";

    String INVALID_LIMIT_PROVIDED = "Limit must be greater than 0";

    String INVALID_SEARCH_PARAM = "The search parameter entered is not long enough";

    String INVALID_DATE_PROVIDED = "Sorry, the date pattern is not accepted. Adopt the format dd-MM-yyyy e.g 30-09-2020";

    String INCOMPLETE_DATE_PARAMS = "Kindly provide both start date and end date";

    String TRANSACTION_NOT_FOUND = "Transaction details not found";

    String TRANSACTION_REF_MISMATCH = "Oops.. its seems we have a transaction ref mismatch";

    String PAYMENT_FAILED = "Payment failed";

    String REMITA_REQUERY_LIMIT = "You can't re-query more than 10 transactions at an instance";

    String AMOUNT_DISCREPANCY = "Amount paid is less than desired amount";

    String EMAIL_ALREADY_REGISTERED = "This email is already registered";

    String EMAIL_OR_PHONE_ALREADY_REGISTERED = "This email or phone number is already registered";

    String USER_REGISTERED_SUCCESSFULLY = "Registration completed. A confirmation email has been sent to you";

    String PASSWORD_LENGTH_TOO_SHORT = "Password length too short a minimum of 8 characters is required";

    String PASSWORD_EMPTY = "Password is required";

    String SPECIAL_CHARACTERS_REQUIRED_FOR_PASSWORD = "Your password must contain at least ONE special character";

    String EMAIL_EMPTY = "Email is required";

    String EMAIL_DOES_NOT_EXIST = "Email address does not exist, please enter your registered email address";

    String USERNAME_EMPTY = "Username is required";

    String FULL_NAME_TOO_SHORT = "FullName is too short a min of 4 character is required";

    String FULL_NAME_EMPTY = "FullName is required";

    String INVALID_PHONE_NUMBER = "Phone number is not valid";

    String PHONE_NUMBER_EMPTY = "Phone number is required";

    String CONTACT_SUPPORT = "Please contact info@boxbybox.ng if error persist";

    String PASSWORD_RESET_REQUEST_SUCCESSFUL = "Password reset link has been sent to your email";

    String PASSWORD_RESET_SUCCESSFUL = "Password reset successful";

    String TOKEN_EXPIRED = "Token has expired please request a new password reset";

    String TOKEN_NOT_PROVIDED = "Token is required";

    String INVALID_TOKEN = "Invalid token";

    String EMAIL_CONFIRMATION_SUCCESSFUL = "Email confirmation successful";

    String ACCOUNT_ALREADY_ACTIVATED = "Account already activated";

    String INVALID_TOKEN_FORMAT = "Invalid token format";

    String ROLE_EXISTS = "A role with this name already exists";

    String INVALID_DESCRIPTION = "Please provide a valid description";

    String INVALID_ROLE_DETAILS = "Invalid role details";

    String ROLE_DEACTIVATED = "Role deactivated";

    String REQUIRE_SUPER_ADMIN_ACCESS = "Only a super administrator can perform this operation";

    String PERMISSION_GROUP_EXISTS = "Permission group already group exists";

    String INVALID_PERMISSION_GROUP_DETAILS = "Invalid permission group details";

    String PERMISSION_EXISTS = "Permission exists";

    String ADDRESS_TYPE_EMPTY = "Address type must be provided";

    String WRONG_ADDRESS_TYPE = "Wrong address type provided";

    String HOUSE_NO_EMPTY = "House no must be provided";

    String STATE_EMPTY = "State must be provided";

    String STATE_NOT_FOUND = "State not found";

    String COUNTRY_EMPTY = "Country must be provided";

    String COUNTRY_NOT_FOUND = "Country not found";

    String NAME_REQUIRED = "Name is required";

    String CODE_REQUIRED = "Code is required";

    String CONTACT_EMAIL_REQUIRED = "Contact email is required";

    String CONTACT_PHONE_REQUIRED = "Contact phone number is required";

    String HEALTH_PROVIDER_EXISTS = "A health provider with this name or code already exists";

    String PASSWORD_LENGTH_UNACCEPTABLE = "Password length must have a minimum of 8 characters";

    String FIRST_NAME_REQUIRED = "First name is required";

    String LAST_NAME_REQUIRED = "Last name is required";

    String ROLE_REQUIRED = "Role details required";

    String ROLE_NOT_FOUND = "Role not found";

    String HEALTH_PROVIDER_EMPTY = "Health provider details empty";

    String HEALTH_FACILITY_NOT_FOUND = "Health facility details not found";

    String HEALTH_ADMIN_NOT_FOUND = "Health admin details not found";

    String USER_NOT_FOUND = "User not found";

    String USER_IS_NOT_A_HEALTH_ADMIN = "User is not a health administrator";

    String NEXT_OF_KIN_FIRST_NAME_REQUIRED = "Next of kin first name is required";

    String NEXT_OF_KIN_LAST_NAME_REQUIRED = "Next of kin last name is required";

    String NEXT_OF_KIN_PHONE_REQUIRED = "Next of kin phone is required";

    String NEXT_OF_KIN_EMAIL_REQUIRED = "Next of kin email is required";

    String NEXT_OF_KIN_RTP_REQUIRED = "Next of kin RTP is required";

    String NEXT_OF_KIN_REQUIRED = "Next of kin details required";

    String MARITAL_STATUS_REQUIRED = "Marital status is required";

    String DOB_REQUIRED = "Date of birth is required";

    String GENDER_REQUIRED = "Gender is required";

    String RELIGION_REQUIRED = "Religion is required";

    String IDENTIFICATION_TYPE_REQUIRED = "Identification type is required";

    String WRONG_IDENTIFICATION_TYPE = "Wrong identification type";

    String EDUCATIONAL_LEVEL_REQUIRED = "Education level is required";

    String PREFERRED_LANGUAGE_REQUIRED = "Preferred language is required";

    String NATIONALITY_REQUIRED = "Nationality is required";

    String USERACCOUNT_DETAILS_REQUIRED = "User account details is required";

    String ADDRESS_DETAILS_REQUIRED = "Address details required";

    String WRONG_BLOOD_GROUP = "Invalid blood group type";

    String PATIENT_HAS_ACTIVE_SURGERY = "This patient already exists with an active surgery process";

    String SURGERY_NOT_FOUND = "Surgery not found";

    String PATIENT_NOT_FOUND = "Patient not found";

    String PATIENT_ALREADY_REGISTERED = "Patient is already registered";

    String PATIENT_ID_REQUIRED = "Patient id required";

    String PATERNAL_ID_REQUIRED = "Paternal id is required";

    String PATERNAL_DETAILS_NOT_FOUND = "Paternal details not found";

    String MATERNAL_ID_REQUIRED = "Maternal id is required";

    String MATERNAL_DETAILS_NOT_FOUND = "Maternal details not found";

    String PATIENT_DATA_ID_REQUIRED = "Patient data id is required";

    String PATIENT_DATA_NOT_FOUND = "Patient data not found";

    String WRONG_ROLE_PARAM = "Wrong role name entered";

    String ACCOUNT_ON_HOLD_MSG = "Your account is ON HOLD. You will not be able to perform this activity. " +
            "Please contact support";

    String PPOLICY_EMPTY = "Kindly agree to our terms before you continue";

    String SOURCE_NEEDED = "Source Needed";

    String WRONG_SOURCE_SELECTED = "Wrong Source Selected";

    String USAGE_NEEDED = "Please enter CREDIT or TREASURY";

    String WRONG_SUSAGE_SELECTED = "";

    String COUNTRY_OF_RESIDENCE_REQUIRED = "";

    String BVN_REQUIRED = "Bvn is required";

    String INCOR_DATE_REQUIRED = "";

    String WRONG_TICKET_STATUS_SELECTED = "wrong ticket status selected";

    String SELECT_TICKET_CATEGORY = "you must select a ticket category";

    String NO_CONTENT = "no message content";

    String NO_USER_SELECTED = "no user selected";

    String TITLE_REQUIRED = "the ticket title is required";

    String SELECT_TICKET_STATUS = "please select a ticket status";

    String CATEGORY_NAME_REQUIRED = "category name for the ticket is required";

    String WRONG_TICKET_CATEGORY_STATUS_SELECTED = "invalid ticket category status selected";

    String WRONG_BANK_ACCOUNT_STATUS = "wrong bank account status selected";

    String NEXT_OF_KIN_NAME_REQUIRED = "Next of Kin's name is required";

    String OCCUPATION_REQUIRED = "Your occupation is required";

    String EMPLOYER_NAME_REQUIRED = "Employer's name is required";

    String EMPLOYER_ADDRESS_REQUIRED = "Employer's address is required";

    String INVALID_NEXT_OF_KIN_EMAIL = "Next of kin email is invalid";

    String NEXT_OF_KIN_PHONE_NUMBER_BLANK = "Next of kin phone number is blank";

    String INVALID_ID_DOCUMENT_TYPE = "Invalid Id document type selected";

    String ACCOUNT_NUMBER_REQUIRED = "Account number is required";

    String BANK_CODE_REQUIRED = "Bank Code is required to get bank details";

    String MUST_BE_TRUE = "Subject consent must be true";

    String ACCOUNT_NAME_NOT_NULL = "Account name cannot be null";

    String PASSPORT_IMAGE_NOT_NULL = "Passport image should not be null, it should be a valid passport photograph";

    String ID_TYPE_REQUIRED = "Id type required";

    String ID_DOCUMENT_IMAGE_REQUIRED = "Id document image required";

    String ID_NUMBER_REQUIRED = "Id number required";

    String OTP_REQUIRED = "otp is required to authorize your request";

    String INVALID_COMPANY_TYPE = "Invalid company type supplied";

    String COMPANY_ADDRESS_REQUIRED = "Company Address required";

    String ENCODED_UPLOAD_REQUIRED = "Encoded upload required";

    String ID_REQUIRED = "Id required";

    String CERTIFICATE_OF_INCO_REQUIRED = "Certificate of incorporation is required";

    String CONTACT_PERSON_PHOTOGRAPH_REQUIRED = "The contact person photograph is required";

    String CONTACT_PERSON_IDENTITY_REQUIRED = "The Contact person's means of identification is required";

    String UTILITY_BILL_REQUIRED = "Utility bill required";

    String SUBJECT_CONSENT_REQUIRED = "Subject consent is required";

    String SOURCE_NAME_REQUIRED = "Source name required";

    String SOURCE_DESCRIPTION_REQUIRED = "Source description required";

    String SOURCE_STATUS_REQUIRED = "Source status required";

    String BRANCH_NAME_REQUIRED = "Branch name required";

    String BRANCH_STATUS_REQUIRED = "Branch status required";

    String DEPT_NAME_REQUIRED = "Department name required";

    String DEPT_STATUS_REQUIRED = "Department status required";

    String ROLE_NAME_REQUIRED = "Role name required";

    String ASSIGNED_USER_ID_REQUIRED = "User Id to be assigned cannot be null";

    String USER_STATUS_REQUIRED = "User status is required";

    String BRANCH_REQUIRED = "Please select a branch";

    String DEPT_REQUIRED = "Please select a department";

    String ASSIGNED_ROLE_REQUIRED = "Please select a role to be assigned";

    String ROLE_NOT_SELECTED = "No role selected, example of role include INDIVIDUAL_USER, COMPANY etc.";

    String INVALID_BVN_DIGIT = "BVN should be 11 Digits";

    String RECIPIENT_NUMBER_REQUIRED = "Recipient's phone number is required";

    String SMS_MESSAGE_REQUIRED = "Sms message required";

    String MODULE_NAME_REQUIRED = "Module name required";

    String ITEM_NAME_REQUIRED = "Module Item name requried";

    String MODULE_ID_REQUIRED = "Module Id required";

    String WITHDRAWAL_AMOUNT_GREATER_THAN_ZERO = "Withdrawal amount must be greater than Zero (0)";

    String WITHDRAWAL_AMOUNT_NOT_NULL = "Withdrawal amount is required";

    String BANK_ACCOUNT_REQUIRED = "Bank Account is required";

    String INVALID_ACCOUNT_NUMBER = "Invalid Account number, A NUBAN account number should be 10 digits";

    String IDENTIFICATION_STATUS_REQUIRED = "Identification Status is required";

    String EMPLOYER_OCCUPATION_REQUIRED = "Employer's Occupation is required";

    String RC_NUMBER_REQUIRED = "Rc Number is required";

    String COMPANY_NAME_REQUIRED = "Company name required";

    String PLAN_NAME_REQUIRED = "Plan name required";

    String PRODUCT_NAME_REQUIRED = "";

    String REQ_TYPE_REQUIRED = "Request type name required";

    String TRANX_TYPE_REQUIRED = "Transaction type is required";

    String WRONG_ACTION = "Wrong action selected";

    String ACTION_REQUIRED = "Action is required";

    String CUSTOMER_REQUEST_CREATION_SUBJECT = "CUSTOMER REQUEST CREATION";

    String CUSTOMER_REQUEST_NEXT_STAGE_SUBJECT = "CUSTOMER REQUEST REVIEWED";

    String CUSTOMER_REQUEST_APPROVED_SUBJECT = "CUSTOMER REQUEST APPROVED";

    String CUSTOMER_REQUEST_DECLINED_SUBJECT = "CUSTOMER REQUEST DECLINED";

    String CUSTOMER_REQUEST_CREATION_MESSAGE_CUSTOMER = "Your request with id {id} has been sent for treatment on the request centre";

    String CUSTOMER_REQUEST_CREATION_MESSAGE_ADMIN = "A request with id {id} is pending your approval on the request centre";

    String CUSTOMER_REQUEST_APPROVAL_FINAL_STAGE_MESSAGE_CUSTOMER = "Your request with id {id} has been approved at the final stage on the request centre";

    String CUSTOMER_REQUEST_APPROVAL_FINAL_STAGE_MESSAGE_ADMIN = "A request with id {id} has been approved on the request centre";

    String CUSTOMER_REQUEST_APPROVAL_NEXT_STAGE_MESSAGE_ADMIN = "A request with id {id} has been sent to your department for review on the request centre";

    String CUSTOMER_REQUEST_APPROVAL_NEXT_STAGE_CUSTOMER_MESSAGE = "Your request with id {id} has been sent to next stage on the request centre";

    String CUSTOMER_REQUEST_DECLINE_PREVIOUS_STAGE_ADMIN_MESSAGE = "A request with id {id} has been sent back from the last stage for further review";

    String CUSTOMER_REQUEST_DECLINE_FINAL_STAGE_ADMIN_MESSAGE = "A request with id {id} has been declined on the request portal";

    String CUSTOMER_REQUEST_DECLINE_PREVIOUS_STAGE_CUSTOMER_MESSAGE = "Your request with id {id} has been sent back to previous stage for further review";

    String CUSTOMER_REQUEST_DECLINE_CUSTOMER_MESSAGE = "Your request with id {id} has been unfortunately been declined";

    String REPORT_FORMAT_REQUIRED = "Report format is required";

    String INVALID_TRANSACTION_TYPE = "Transaction type is invalid";

    String INVALID_REPORT_FORMAT = "Report format must be pdf, xlsx, or csv";

    String USSD_CODE_DESCRIPTION_REQUIRED = "Ussd code and description is required";

    String BANK_ID_REQUIRED = "Bank id is required";

    String MODULE_ITEM_NAME_REQUIRED = "Module item name is required";

    String MONTH_REQUIRED = "A valid month is required eg. DECEMBER";
}
