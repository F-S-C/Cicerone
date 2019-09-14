<?php

namespace db_connector;

require_once("InsertConnector.php");

/**
 * Insert an itinerary.
 * @package db_connector
 */
class InsertItinerary extends InsertConnector
{
    protected const COLUMNS = "title, description, beginning_date, ending_date, end_reservations_date, maximum_participants_number, minimum_participants_number, location, repetitions_per_day, duration, username, full_price, reduced_price, image_url";
    protected const COLUMNS_TYPE = "sssssiisissdds";
    //TODO: Add image url into query
    protected const TABLE_NAME = "itinerary";
}

$connector = new InsertItinerary();
$connector->add_value(array($_POST['title'],
    $_POST['description'],
    $_POST['beginning_date'],
    $_POST['ending_date'],
    $_POST['end_reservations_date'],
    $_POST['maximum_participants_number'],
    $_POST['minimum_participants_number'],
    $_POST['location'],
    $_POST['repetitions_per_day'],
    $_POST['duration'],
    strtolower($_POST['username']),
    $_POST['full_price'],
    $_POST['reduced_price'],
    $_POST['image_url']
));
print $connector->get_content();