<?php

namespace database_connector\controller\insert;

require_once "InsertConnector.php";

/**
 * Insert a review for an itinerary.
 * @package database_connector\controller\insert
 */
class InsertItineraryReview extends InsertConnector
{
    protected const COLUMNS = "username, reviewed_itinerary, feedback, description";
    protected const COLUMNS_TYPE = "ssis";
    protected const TABLE_NAME = "itinerary_review";
}