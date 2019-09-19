package app_connector;

/**
 * Connector Constants for Cicerone's connector.
 */
public final class ConnectorConstants {
    //TODO: Check all constants and remove duplicates.
    private ConnectorConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    private static final String SERVER_URL = "https://fscgroup.ddns.net";
    private static final String DB_CONNECTOR_FOLDER = "/database_connector/view";

    private static final String INSERT_SUBFOLDER = "/insert";
    private static final String DELETE_SUBFOLDER = "/delete";
    private static final String UPDATE_SUBFOLDER = "/update";
    private static final String REQUEST_SUBFOLDER = "/request";
    private static final String DOWNLOAD_SUBFOLDER = "/download";


    /**
     * URL of the server-side connector for the login.
     */
    public static final String LOGIN_CONNECTOR = SERVER_URL + DB_CONNECTOR_FOLDER + REQUEST_SUBFOLDER + "/UserLogin.php";
    /**
     * URL of the server-side connector for the insertion of an itinerary.
     */
    public static final String INSERT_ITINERARY = SERVER_URL + DB_CONNECTOR_FOLDER + INSERT_SUBFOLDER + "/InsertItinerary.php";

    /**
     * URL of the server-side connector for the registered_user table.
     */
    public static final String REGISTERED_USER = SERVER_URL + DB_CONNECTOR_FOLDER + REQUEST_SUBFOLDER + "/RequestRegisteredUser.php";

    /**
     * URL of the server-side connector for the report JOIN report_details query.
     */
    public static final String REPORT_FRAGMENT = SERVER_URL + DB_CONNECTOR_FOLDER + REQUEST_SUBFOLDER + "/RequestReportJoinReportDetails.php";

    /**
     * URL of the server-side connector for the itinerary table.
     */
    public static final String REQUEST_ITINERARY = SERVER_URL + DB_CONNECTOR_FOLDER + REQUEST_SUBFOLDER + "/RequestItinerary.php";

    /**
     * URL of the server-side connector for the itinerary table.
     */
    public static final String REQUEST_ACTIVE_ITINERARY = SERVER_URL + DB_CONNECTOR_FOLDER + REQUEST_SUBFOLDER + "/RequestActiveItinerary.php";

    /**
     * URL of the server-side connector for the itinerary_review table.
     */
    public static final String ITINERARY_REVIEW = SERVER_URL + DB_CONNECTOR_FOLDER + REQUEST_SUBFOLDER + "/RequestItineraryReview.php";

    /**
     * URL of the server-side connector for the itinerary_review table.
     */
    public static final String REQUEST_RESERVATION = SERVER_URL + DB_CONNECTOR_FOLDER + REQUEST_SUBFOLDER + "/RequestReservation.php";

    /**
     * URL of the server-side connector for the update of registered_user table.
     */
    public static final String UPDATE_REGISTERED_USER = SERVER_URL + DB_CONNECTOR_FOLDER + UPDATE_SUBFOLDER + "/UpdateRegisteredUser.php";

    /**
     * URL of the server-side connector for the download of the user's data.
     */
    public static final String DOWNLOAD_USER_DATA = SERVER_URL + DB_CONNECTOR_FOLDER + DOWNLOAD_SUBFOLDER + "/UserDataDownloader.php";

    /**
     * URL of the server-side connector for the itinerary table.
     */
    public static final String DELETE_ITINERARY = SERVER_URL + DB_CONNECTOR_FOLDER + DELETE_SUBFOLDER + "/DeleteItinerary.php";

    /**
     * URL of the server-side connector for the itinerary table.
     */
    public static final String UPDATE_ITINERARY = SERVER_URL + DB_CONNECTOR_FOLDER + UPDATE_SUBFOLDER + "/UpdateItinerary.php";

    /**
     * URL of the server-side connector for the wishlist table.
     */
    public static final String INSERT_WISHLIST = SERVER_URL + DB_CONNECTOR_FOLDER + INSERT_SUBFOLDER + "/InsertWishlist.php";

    /**
     * URL of the server-side connector for the wishlist table.
     */
    public static final String DELETE_WISHLIST = SERVER_URL + DB_CONNECTOR_FOLDER + DELETE_SUBFOLDER + "/DeleteWishlist.php";

    /**
     * URL of the server-side connector for the wishlist table.
     */
    public static final String CLEAR_WISHLIST = SERVER_URL + DB_CONNECTOR_FOLDER + DELETE_SUBFOLDER + "/ClearWishlist.php";

    /**
     * URL of the server-side connector for the wishlist table.
     */
    public static final String REQUEST_WISHLIST = SERVER_URL + DB_CONNECTOR_FOLDER + REQUEST_SUBFOLDER + "/RequestWishlist.php";

    /**
     * URL of the server-side connector for the wishlist table.
     */
    public static final String SEARCH_WISHLIST = SERVER_URL + DB_CONNECTOR_FOLDER + REQUEST_SUBFOLDER + "/RequestWishlist.php"; //TODO: remove from class?

    /**
     * URL of the server-side connector for the user_review table.
     */
    public static final String REQUEST_USER_REVIEW = SERVER_URL + DB_CONNECTOR_FOLDER + REQUEST_SUBFOLDER + "/RequestUserReview.php";

    /**
     * URL of the server-side connector for the user_review table.
     */
    public static final String REQUEST_ITINERARY_REVIEW = SERVER_URL + DB_CONNECTOR_FOLDER + REQUEST_SUBFOLDER + "/RequestItineraryReview.php";

    /**
     * URL of the server-side connector for the user_review table
     */
    public static final String INSERT_USER_REVIEW = SERVER_URL + DB_CONNECTOR_FOLDER + INSERT_SUBFOLDER + "/InsertUserReview.php";

    /**
     * URL of the server-side connector for the itinerary_review table
     */
    public static final String INSERT_ITINERARY_REVIEW = SERVER_URL + DB_CONNECTOR_FOLDER + INSERT_SUBFOLDER + "/InsertItineraryReview.php";

    /**
     * URL of the server-side connector for the itinerary_review table
     */
    public static final String DELETE_ITINERARY_REVIEW = SERVER_URL + DB_CONNECTOR_FOLDER + DELETE_SUBFOLDER + "/DeleteItineraryReview.php";

    /**
     * URL of the server-side connector for the itinerary_review table
     */
    public static final String UPDATE_ITINERARY_REVIEW = SERVER_URL + DB_CONNECTOR_FOLDER + UPDATE_SUBFOLDER + "/UpdateItineraryReview.php";

    /**
     * URL of the server-side connector for the user_review table
     */
    public static final String UPDATE_USER_REVIEW = SERVER_URL + DB_CONNECTOR_FOLDER + UPDATE_SUBFOLDER + "/UpdateUserReview.php";

    /**
     * URL of the server-side connector for the user_review table.
     */
    public static final String DELETE_USER_REVIEW = SERVER_URL + DB_CONNECTOR_FOLDER + DELETE_SUBFOLDER + "/DeleteUserReview.php";

    /**
     * URL of the server-side connector for the document table.
     */
    public static final String REQUEST_DOCUMENT = SERVER_URL + DB_CONNECTOR_FOLDER + REQUEST_SUBFOLDER + "/RequestDocument.php";

    /**
     * URL of the server-side connector for the document table.
     */
    public static final String UPDATE_DOCUMENT = SERVER_URL + DB_CONNECTOR_FOLDER + UPDATE_SUBFOLDER + "/Updatecument.php";

    /**
     * URL of the server-side connector for the report JOIN report_details query.
     */
    public static final String REQUEST_FOR_REVIEW = SERVER_URL + DB_CONNECTOR_FOLDER + REQUEST_SUBFOLDER + "/CheckIfUserCanReviewUser.php";

    /**
     * URL of the server-side connector to delete a registered user.
     */
    public static final String DELETE_REGISTERED_USER = SERVER_URL + DB_CONNECTOR_FOLDER + DELETE_SUBFOLDER + "/DeleteRegisteredUser.php";

    /**
     * URL of the server-side connector for the languages table.
     */
    public static final String REQUEST_LANGUAGES = SERVER_URL + DB_CONNECTOR_FOLDER + REQUEST_SUBFOLDER + "/RequestLanguage.php";

    /**
     * URL of the server-side connector for the user_language table.
     */
    public static final String INSERT_USER_LANGUAGE = SERVER_URL + DB_CONNECTOR_FOLDER + INSERT_SUBFOLDER + "/InsertUserLanguage.php";

    /**
     * URL of the server-side connector for the user table.
     */
    public static final String INSERT_USER = SERVER_URL + DB_CONNECTOR_FOLDER + INSERT_SUBFOLDER + "/InsertRegisteredUser.php";

    /**
     * URL of the server-side connector for the document table.
     */
    public static final String INSERT_DOCUMENT = SERVER_URL + DB_CONNECTOR_FOLDER + INSERT_SUBFOLDER + "/InsertDocument.php";

    /**
     * URL of the server-side connector to insert a new reservation.
     */
    public static final String INSERT_RESERVATION = SERVER_URL + DB_CONNECTOR_FOLDER + INSERT_SUBFOLDER + "/InsertReservation.php";

    /**
     * URL of the server-side connector for the report details table.
     */
    public static final String UPDATE_REPORT_DETAILS = SERVER_URL + DB_CONNECTOR_FOLDER + UPDATE_SUBFOLDER + "/UpdateReportDetails.php";

    /**
     * URL of the server-side connector to remove a reservation.
     */
    public static final String DELETE_RESERVATION = SERVER_URL + DB_CONNECTOR_FOLDER + DELETE_SUBFOLDER + "/DeleteReservation.php";

    /**
     * URL of the server-side connector for the reservations and itineraries details table.
     */
    public static final String REQUEST_RESERVATION_JOIN_ITINERARY = SERVER_URL + DB_CONNECTOR_FOLDER + REQUEST_SUBFOLDER + "/RequestItineraryJoinReservation.php";

    /**
     * URL of the server-side connector for the report details table.
     */
    public static final String INSERT_REPORT = SERVER_URL + DB_CONNECTOR_FOLDER + INSERT_SUBFOLDER + "/InsertReport.php";

    /**
     * URL of the server-side connector for the report details table.
     */
    public static final String INSERT_REPORT_DETAILS = SERVER_URL + DB_CONNECTOR_FOLDER + INSERT_SUBFOLDER + "/InsertReportDetails.php";

    /**
     * URL of the server-side connector for updating a reservation.
     */
    public static final String UPDATE_RESERVATION = SERVER_URL + DB_CONNECTOR_FOLDER + UPDATE_SUBFOLDER + "/UpdateReservation.php";

    /**
     * URL of the script that takes care of sending the e-mails.
     */
    public static final String EMAIL_SENDER = SERVER_URL + "/email_sender/sender.php";

    /**
     * URL of the script that takes care of uploading the images to the server.
     */
    public static final String IMAGE_UPLOADER = SERVER_URL + "/img_uploader/Uploader.php";

    /**
     * URL of the images folder on the server.
     */
    public static final String IMG_FOLDER = SERVER_URL + "/images/";
}


