<?php

namespace db_connector;

require_once "InsertConnector.php";

/**
 * Insert a review for an itinerary.
 * @package db_connector
 */
class InsertItineraryReview extends InsertConnector
{
    protected const COLUMNS = "username, reviewed_itinerary, feedback, description";
    protected const COLUMNS_TYPE = "ssis";
    protected const TABLE_NAME = "itinerary_review";
}

$connector = new InsertItineraryReview();
$connector->add_value(array(strtolower($_POST['username']),
    $_POST['reviewed_itinerary'],
    $_POST['feedback'],
    $_POST['description']
));
print $connector->get_content();