<?php

namespace database_connector\view\update;

use database_connector\controller\update\UpdateReservation;

require_once "/membri/fsc/database_connector/controller/update/UpdateReservation.php";

$connector = new UpdateReservation(json_decode($_POST['username'], true)['username'], json_decode($_POST['booked_itinerary'], true)['itinerary_code']);
$connector->add_value("confirm_date", $_POST['confirm_date'], "s");
print $connector->get_content();