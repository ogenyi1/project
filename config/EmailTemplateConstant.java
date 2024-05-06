package ng.optisoft.rosabon.config;


public enum  EmailTemplateConstant {


    TEST_TEMPLATE("test.html"),
    NOTIFICATION_TEMPLATE("notification.html"),
    NOTIFICATION_TEMPLATE_CREDIT("notification-credit.html"),
    USER_REGISTRATION_TEMPLATE("user-registration.html"),
    USER_REGISTRATION_TEMPLATE_CREDIT("user-registration-credit.html");


    private final String description;

    EmailTemplateConstant(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
