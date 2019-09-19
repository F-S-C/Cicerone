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
    private static final String DB_CONNECTOR_FOLDER = "/db_interface";


    /**
     * URL of the server-side connector for the login.
     */
    public static final String LOGIN_CONNECTOR = SERVER_URL + DB_CONNECTOR_FOLDER + "/UserLogin.php";
    /**
     * URL of the server-side connector for the insertion of an itinerary.
     */
    public static final String INSERT_ITINERARY = SERVER_URL + DB_CONNECTOR_FOLDER + "/InsertItinerary.php";

    /**
     * URL of the server-side connector for the registered_user table.
     */
    public static final String REGISTERED_USER = SERVER_URL + DB_CONNECTOR_FOLDER + "/DoRequestRegisteredUser.php";

    /**
     * URL of the server-side connector for the report JOIN report_details query.
     */
    public static final String REPORT_FRAGMENT = SERVER_URL + DB_CONNECTOR_FOLDER + "/DoRequestReportJoinReportDetails.php";

    /**
     * URL of the server-side connector for the itinerary table.
     */
    public static final String REQUEST_ITINERARY = SERVER_URL + DB_CONNECTOR_FOLDER + "/DoRequestItinerary.php";

    /**
     * URL of the server-side connector for the itinerary table.
     */
    public static final String REQUEST_ACTIVE_ITINERARY = SERVER_URL + DB_CONNECTOR_FOLDER + "/DoRequestActiveItinerary.php";

    /**
     * URL of the server-side connector for the itinerary_review table.
     */
    public static final String ITINERARY_REVIEW = SERVER_URL + DB_CONNECTOR_FOLDER + "/DoRequestItineraryReview.php";

    /**
     * URL of the server-side connector for the itinerary_review table.
     */
    public static final String REQUEST_RESERVATION = SERVER_URL + DB_CONNECTOR_FOLDER + "/DoRequestReservation.php";

    /**
     * URL of the server-side connector for the update of registered_user table.
     */
    public static final String UPDATE_REGISTERED_USER = SERVER_URL + DB_CONNECTOR_FOLDER + "/UpdateRegisteredUser.php";

    /**
     * URL of the server-side connector for the download of the user's data.
     */
    public static final String DOWNLOAD_USER_DATA = SERVER_URL + DB_CONNECTOR_FOLDER + "/UserDataDownloader.php";

    /**
     * URL of the server-side connector for the itinerary table.
     */
    public static final String DELETE_ITINERARY = SERVER_URL + DB_CONNECTOR_FOLDER + "/DeleteItinerary.php";

    /**
     * URL of the server-side connector for the itinerary table.
     */
    public static final String UPDATE_ITINERARY = SERVER_URL + DB_CONNECTOR_FOLDER + "/UpdateItinerary.php";

    /**
     * URL of the server-side connector for the wishlist table.
     */
    public static final String INSERT_WISHLIST = SERVER_URL + DB_CONNECTOR_FOLDER + "/InsertWishlist.php";

    /**
     * URL of the server-side connector for the wishlist table.
     */
    public static final String DELETE_WISHLIST = SERVER_URL + DB_CONNECTOR_FOLDER + "/DeleteWishlist.php";

    /**
     * URL of the server-side connector for the wishlist table.
     */
    public static final String CLEAR_WISHLIST = SERVER_URL + DB_CONNECTOR_FOLDER + "/ClearWishlist.php";

    /**
     * URL of the server-side connector for the wishlist table.
     */
    public static final String REQUEST_WISHLIST = SERVER_URL + DB_CONNECTOR_FOLDER + "/DoRequestWishlist.php";

    /**
     * URL of the server-side connector for the wishlist table.
     */
    public static final String SEARCH_WISHLIST = SERVER_URL + DB_CONNECTOR_FOLDER + "/DoRequestWishlist.php"; //TODO: remove from class?

    /**
     * URL of the server-side connector for the user_review table.
     */
    public static final String REQUEST_USER_REVIEW = SERVER_URL + DB_CONNECTOR_FOLDER + "/DoRequestUserReview.php";

    /**
     * URL of the server-side connector for the user_review table.
     */
    public static final String REQUEST_ITINERARY_REVIEW = SERVER_URL + DB_CONNECTOR_FOLDER + "/DoRequestItineraryReview.php";

    /**
     * URL of the server-side connector for the user_review table
     */
    public static final String INSERT_USER_REVIEW = SERVER_URL + DB_CONNECTOR_FOLDER + "/InsertUserReview.php";

    /**
     * URL of the server-side connector for the itinerary_review table
     */
    public static final String INSERT_ITINERARY_REVIEW = SERVER_URL + DB_CONNECTOR_FOLDER + "/InsertItineraryReview.php";

    /**
     * URL of the server-side connector for the itinerary_review table
     */
    public static final String DELETE_ITINERARY_REVIEW = SERVER_URL + DB_CONNECTOR_FOLDER + "/DeleteItineraryReview.php";

    /**
     * URL of the server-side connector for the itinerary_review table
     */
    public static final String UPDATE_ITINERARY_REVIEW = SERVER_URL + DB_CONNECTOR_FOLDER + "/UpdateItineraryReview.php";

    /**
     * URL of the server-side connector for the user_review table
     */
    public static final String UPDATE_USER_REVIEW = SERVER_URL + DB_CONNECTOR_FOLDER + "/UpdateUserReview.php";

    /**
     * URL of the server-side connector for the user_review table.
     */
    public static final String DELETE_USER_REVIEW = SERVER_URL + DB_CONNECTOR_FOLDER + "/DeleteUserReview.php";

    /**
     * URL of the server-side connector for the document table.
     */
    public static final String REQUEST_DOCUMENT = SERVER_URL + DB_CONNECTOR_FOLDER + "/DoRequestDocument.php";

    /**
     * URL of the server-side connector for the document table.
     */
    public static final String UPDATE_DOCUMENT = SERVER_URL + DB_CONNECTOR_FOLDER + "/UpdateDocument.php";

    /**
     * URL of the server-side connector for the report JOIN report_details query.
     */
    public static final String REQUEST_FOR_REVIEW = SERVER_URL + DB_CONNECTOR_FOLDER + "/CheckIfUserCanReviewUser.php";

    /**
     * URL of the server-side connector to delete a registered user.
     */
    public static final String DELETE_REGISTERED_USER = SERVER_URL + DB_CONNECTOR_FOLDER + "/DeleteRegisteredUser.php";

    /**
     * URL of the server-side connector for the languages table.
     */
    public static final String REQUEST_LANGUAGES = SERVER_URL + DB_CONNECTOR_FOLDER + "/DoRequestLanguage.php";

    /**
     * URL of the server-side connector for the user_language table.
     */
    public static final String INSERT_USER_LANGUAGE = SERVER_URL + DB_CONNECTOR_FOLDER + "/InsertUserLanguage.php";

    /**
     * URL of the server-side connector for the user table.
     */
    public static final String INSERT_USER = SERVER_URL + DB_CONNECTOR_FOLDER + "/InsertRegisteredUser.php";

    /**
     * URL of the server-side connector for the document table.
     */
    public static final String INSERT_DOCUMENT = SERVER_URL + DB_CONNECTOR_FOLDER + "/InsertDocument.php";

    /**
     * URL of the server-side connector to insert a new reservation.
     */
    public static final String INSERT_RESERVATION = SERVER_URL + DB_CONNECTOR_FOLDER + "/InsertReservation.php";

    /**
     * URL of the server-side connector for the report details table.
     */
    public static final String UPDATE_REPORT_DETAILS = SERVER_URL + DB_CONNECTOR_FOLDER + "/UpdateReportDetails.php";

    /**
     * URL of the server-side connector to remove a reservation.
     */
    public static final String DELETE_RESERVATION = SERVER_URL + DB_CONNECTOR_FOLDER + "/DeleteReservation.php";

    /**
     * URL of the server-side connector for the reservations and itineraries details table.
     */
    public static final String REQUEST_RESERVATION_JOIN_ITINERARY = SERVER_URL + DB_CONNECTOR_FOLDER + "/DoRequestItineraryJoinReservation.php";

    /**
     * URL of the server-side connector for the report details table.
     */
    public static final String INSERT_REPORT = SERVER_URL + DB_CONNECTOR_FOLDER + "/InsertReport.php";

    /**
     * URL of the server-side connector for the report details table.
     */
    public static final String INSERT_REPORT_DETAILS = SERVER_URL + DB_CONNECTOR_FOLDER + "/InsertReportDetails.php";

    /**
     * URL of the script that takes care of sending the e-mails.
     */
    public static final String EMAIL_SENDER = SERVER_URL + "/email_sender/sender.php";

    /** 
     * URL of the server-side connector for updating a reservation.
     */
    public static final String UPDATE_RESERVATION = SERVER_URL + DB_CONNECTOR_FOLDER + "/UpdateReservation.php";

    /**
     * URL of the script that takes care of uploading the images to the server.
     */
    public static final String IMAGE_UPLOADER = SERVER_URL + "/img_uploader/Uploader.php";

    /**
     * URL of the images folder on the server.
     */
    public static final String IMG_FOLDER = SERVER_URL + "/images/";
}


