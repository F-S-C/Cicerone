package app_connector;

/**
 * Connector Constants for Cicerone's connector.
 */
public final class ConnectorConstants {
    private ConnectorConstants() {
    }

    private static final String SERVER_URL = "https://fscgroup.ddns.net/db_connector";

    /**
     * URL of the server-side connector for the login
     */
    public static final String LOGIN_CONNECTOR = SERVER_URL + "/user_login.php";
    /**
     * URL of the server-side connector for the insertion of an itinerary
     */
    public static final String INSERT_ITINERARY = SERVER_URL + "/insert_itinerary.php";

    /**
     * URL of the server-side connector for the registered_user table
     */
    public static final String REGISTERED_USER = SERVER_URL + "/request_registered_user.php";

    /**
     * URL of the server-side connector for the report JOIN report_details query
     */
    public static final String REPORT_FRAGMENT = SERVER_URL + "/request_report_JOIN_report_details.php";

    /**
     * URL of the server-side connector for the itinerary table
     */
    public static final String REQUEST_ITINERARY = SERVER_URL + "/request_itinerary.php";

    /**
     * URL of the server-side connector for the user_review table
     */
    public static final String USER_REVIEW = SERVER_URL + "/request_user_review.php";
}


