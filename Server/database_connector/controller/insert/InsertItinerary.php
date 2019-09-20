<?php

namespace database_connector\controller\insert;

require_once "/membri/fsc/database_connector/controller/insert/InsertConnector.php";

/**
 * Insert an itinerary.
 * @package database_connector\controller\insert
 */
class InsertItinerary extends InsertConnector
{
    protected const COLUMNS = "title, description, beginning_date, ending_date, end_reservations_date, maximum_participants_number, minimum_participants_number, location, repetitions_per_day, duration, username, full_price, reduced_price, image_url";
    protected const COLUMNS_TYPE = "sssssiisissdds";
    protected const TABLE_NAME = "itinerary";
}