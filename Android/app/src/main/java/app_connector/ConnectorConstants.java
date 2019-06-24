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
}
