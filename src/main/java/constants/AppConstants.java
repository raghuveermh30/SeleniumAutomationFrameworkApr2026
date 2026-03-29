package constants;

public abstract class AppConstants {

    public static final String LOGIN_PAGE_TITLE = "Account Login";
    public static final String LOGIN_PAGE_URL_FRACTION = "route=account/login";
    public static final String HOME_PAGE_TITLE = "My Account";
    public static final String HOME_PAGE_URL_FRACTION = "route=account/account";

    public static final int DEFAULT_TIME_OUT = 5;
    public static final int SHORT_TIME_OUT = 10;
    public static final int MEDIUM_TIME_OUT = 15;
    public static final int MAX_TIME_OUT = 25;

    public static final String CONFIG_PROD_PROP_FILE_PATH = "./src/test/resources/config/config.properties";
    public static final String CONFIG_QA_PROP_FILE_PATH = "./src/test/resources/config/qa.properties";
    public static final String CONFIG_UAT_PROP_FILE_PATH = "./src/test/resources/config/uat.properties";
    public static final String CONFIG_STAGE_PROP_FILE_PATH = "./src/test/resources/config/stage.properties";

    public static final String PRODUCT_SHEET_NAME = "Product";
    public static final String REGISTRATION_SHEET_NAME = "Users";

}
