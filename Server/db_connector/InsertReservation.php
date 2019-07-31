<?php

namespace db_connector;

require_once("InsertConnector.php");

class InsertReservation extends InsertConnector
{
    protected const COLUMNS = "username, booked_itinerary, number_of_children, number_of_adults, total, requested_date, forwading_date, confirm_date";
    protected const COLUMNS_TYPE = "siiidsss";
    protected const TABLE_NAME = "reservation";
}

$connector = new InsertReservation();
$connector->add_value(array(strtolower($_POST['username']),
    $_POST['booked_itinerary'],
    $_POST['number_of_children'],
    $_POST['number_of_adults'],
    $_POST['total'],
    $_POST['requested_date'],
    $_POST['forwarding_date'],
    $_POST['confirm_date']
));
print $connector->get_content();