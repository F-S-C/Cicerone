<?php

namespace database_connector\view\insert;

use database_connector\controller\insert\InsertReservation;

require_once "/membri/fsc/database_connector/controller/insert/InsertReservation.php";

$connector = new InsertReservation();
$connector->add_value(array(strtolower(json_decode($_POST['username'], true)["username"]),
    json_decode($_POST['booked_itinerary'], true)["itinerary_code"],
    $_POST['number_of_children'],
    $_POST['number_of_adults'],
    $_POST['total'],
    $_POST['requested_date'],
    $_POST['forwarding_date'],
    $_POST['confirm_date']
));
print $connector->get_content();