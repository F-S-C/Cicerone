<?php

namespace database_connector\controller\insert;

require_once "/home/fsc/www/database_connector/controller/insert/InsertConnector.php";

/**
 * Insert a review for an itinerary.
 */
class InsertItineraryReview extends InsertConnector
{
    protected const COLUMNS = "username, reviewed_itinerary, feedback, description";
    protected const COLUMNS_TYPE = "ssis";
    protected const TABLE_NAME = "itinerary_review";
}