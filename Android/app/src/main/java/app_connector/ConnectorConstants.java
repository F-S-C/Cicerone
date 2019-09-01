package app_connector;

/**
 * Connector Constants for Cicerone's connector.
 */
public final class ConnectorConstants {
    private ConnectorConstants() {
    }

    private static final String SERVER_URL = "https://fscgroup.ddns.net/db_connector";

    /**
     * URL of the server-side connector for the login.
     */
    public static final String LOGIN_CONNECTOR = SERVER_URL + "/UserLogin.php";
    /**
     * URL of the server-side connector for the insertion of an itinerary.
     */
    public static final String INSERT_ITINERARY = SERVER_URL + "/InsertItinerary.php";

    /**
     * URL of the server-side connector for the registered_user table.
     */
    public static final String REGISTERED_USER = SERVER_URL + "/RequestRegisteredUser.php";

    /**
     * URL of the server-side connector for the report JOIN report_details query.
     */
    public static final String REPORT_FRAGMENT = SERVER_URL + "/RequestReportJoinReportDetails.php";

    /**
     * URL of the server-side connector for the itinerary table.
     */
    public static final String REQUEST_ITINERARY = SERVER_URL + "/RequestItinerary.php";

    /**
     * URL of the server-side connector for the itinerary table.
     */
    public static final String REQUEST_ACTIVE_ITINERARY = SERVER_URL + "/RequestActiveItinerary.php";

    /**
     * URL of the server-side connector for the itinerary_review table.
     */
    public static final String ITINERARY_REVIEW = SERVER_URL + "/RequestItineraryReview.php";

    /**
     * URL of the server-side connector for the itinerary_review table.
     */
    public static final String REQUEST_RESERVATION = SERVER_URL + "/RequestReservation.php";

    /**
     * URL of the server-side connector for the update of registered_user table.
     */
    public static final String UPDATE_REGISTERED_USER = SERVER_URL + "/UpdateRegisteredUser.php";

    /**
     * URL of the server-side connector for the download of the user's data.
     */
    public static final String DOWNLOAD_USER_DATA = SERVER_URL + "/UserDataDownloader.php";

    /**
     * URL of the server-side connector for the itinerary table.
     */
    public static final String DELETE_ITINERARY = SERVER_URL + "/DeleteItinerary.php";

    /**
     * URL of the server-side connector for the itinerary table.
     */
    public static final String UPDATE_ITINERARY = SERVER_URL + "/UpdateItinerary.php";

    /**
     * URL of the server-side connector for the wishlist table.
     */
    public static final String INSERT_WISHLIST = SERVER_URL + "/InsertWishlist.php";

    /**
     * URL of the server-side connector for the wishlist table.
     */
    public static final String DELETE_WISHLIST = SERVER_URL + "/DeleteWishlist.php";

    /**
     * URL of the server-side connector for the wishlist table.
     */
    public static final String CLEAR_WISHLIST = SERVER_URL + "/ClearWishlist.php";

    /**
     * URL of the server-side connector for the wishlist table.
     */
    public static final String REQUEST_WISHLIST = SERVER_URL + "/RequestWishlistJoinItinerary.php";

    /**
     * URL of the server-side connector for the wishlist table.
     */
    public static final String SEARCH_WISHLIST = SERVER_URL + "/RequestWishlist.php";

    /**
     * URL of the server-side connector for the user_review table.
     */
    public static final String REQUEST_USER_REVIEW = SERVER_URL + "/RequestUserReview.php";

    /**
     * URL of the server-side connector for the user_review table.
     */
    public static final String REQUEST_ITINERARY_REVIEW = SERVER_URL + "/RequestItineraryReview.php";

    /**
     * URL of the server-side connector for the user_review table
     */
    public static final String INSERT_USER_REVIEW = SERVER_URL + "/InsertUserReview.php";

    /**
     * URL of the server-side connector for the itinerary_review table
     */
    public static final String INSERT_ITINERARY_REVIEW = SERVER_URL + "/InsertItineraryReview.php";

    /**
     * URL of the server-side connector for the itinerary_review table
     */
    public static final String DELETE_ITINERARY_REVIEW = SERVER_URL + "/DeleteItineraryReview.php";

    /**
     * URL of the server-side connector for the itinerary_review table
     */
    public static final String UPDATE_ITINERARY_REVIEW = SERVER_URL + "/UpdateItineraryReview.php";

    /**
     * URL of the server-side connector for the user_review table
     */
    public static final String UPDATE_USER_REVIEW = SERVER_URL + "/UpdateUserReview.php";

    /**
     * URL of the server-side connector for the user_review table.
     */
    public static final String DELETE_USER_REVIEW = SERVER_URL + "/DeleteUserReview.php";

    /**
     * URL of the server-side connector for the document table.
     */
    public static final String REQUEST_DOCUMENT = SERVER_URL + "/RequestDocument.php";

    /**
     * URL of the server-side connector for the document table.
     */
    public static final String UPDATE_DOCUMENT = SERVER_URL + "/UpdateDocument.php";

    /**
     * URL of the server-side connector for the report JOIN report_details query.
     */
    public static final String REQUEST_FOR_REVIEW = SERVER_URL + "/CheckIfUserCanReviewUser.php";

    /**
     * URL of the server-side connector to delete a registered user.
     */
    public static final String DELETE_REGISTERED_USER = SERVER_URL + "/DeleteRegisteredUser.php";

    /**
     * URL of the server-side connector for the languages table.
     */
    public static final String REQUEST_LANGUAGES = SERVER_URL + "/RequestLanguage.php";

    /**
     * URL of the server-side connector for the user_language table.
     */
    public static final String INSERT_USER_LANGUAGE = SERVER_URL + "/InsertUserLanguage.php";

    /**
     * URL of the server-side connector for the user table.
     */
    public static final String INSERT_USER = SERVER_URL + "/InsertRegisteredUser.php";

    /**
     * URL of the server-side connector for the document table.
     */
    public static final String INSERT_DOCUMENT = SERVER_URL + "/InsertDocument.php";

    /**
     * URL of the server-side connector to insert a new reservation.
     */
    public static final String INSERT_RESERVATION = SERVER_URL + "/InsertReservation.php";

    /**
     * URL of the server-side connector for the report details table.
     */
    public static final String UPDATE_REPORT_DETAILS = SERVER_URL + "/UpdateReportDetails.php";

    /**
     * URL of the server-side connector to remove a reservation.
     */
    public static final String DELETE_RESERVATION = SERVER_URL + "/DeleteReservation.php";
}


