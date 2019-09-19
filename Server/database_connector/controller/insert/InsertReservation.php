<?php

namespace database_connector\controller\insert;

require_once "InsertConnector.php";

/**
 * Insert a reservation for an itinerary.
 * @package database_connector\controller\insert
 */
class InsertReservation extends InsertConnector
{
    protected const COLUMNS = "username, booked_itinerary, number_of_children, number_of_adults, total, requested_date, forwading_date, confirm_date";
    protected const COLUMNS_TYPE = "siiidsss";
    protected const TABLE_NAME = "reservation";
}