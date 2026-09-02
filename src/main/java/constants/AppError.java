package constants;

/**
 * Holds standard assertion/error message strings used across test classes
 * to keep failure messages consistent.
 */
public abstract class AppError {

    public static final String LOGIN_PAGE_TITLE_ERROR = "Login page title is not correct";
    public static final String LOGIN_PAGE_URL_ERROR = "Login page url is not correct";
    public static final String FORGOT_PWD_LINK_ERROR = "Forgot pwd link is not exist on the page";
    public static final String HOME_PAGE_TITLE_ERROR = "Home Page Title is not Matched";
    public static final String ELEMENT_NOT_FOUND_ERROR = "Element is not found on the page";
    public static final String ELEMENT_NOT_VISIBLE_ERROR = "Element is not visible on the page";
    public static final String ELEMENT_NOT_CLICKABLE_ERROR = "Element is not clickable on the page";

    public static final String LOGO_NOT_DISPLAYED_ERROR = "Logo is not displayed on the page";
    public static final String INVALID_LOGIN_ERROR = "Login warning message is not displayed for invalid credentials";
    public static final String HOME_PAGE_HEADERS_EMPTY_ERROR = "Home page headers list should not be empty after login";
    public static final String LOGOUT_MSG_NOT_DISPLAYED_ERROR = "Logout message is not displayed after logout";
    public static final String PRODUCT_DETAILS_EMPTY_ERROR = "Product details list should not be empty";
    public static final String REGISTRATION_VALIDATION_ERROR = "Validation errors should be displayed";
}
