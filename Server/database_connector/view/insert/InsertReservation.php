<?php

namespace database_connector\view\insert;

use database_connector\controller\insert\InsertReservation;

require_once "../../controller/insert/InsertReservation.php";

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