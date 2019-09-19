<?php

namespace db_interface\update;

use db_connector\update\UpdateReservation;

require_once "../../db_connector/update/UpdateReservation.php";

$connector = new UpdateReservation($_POST['username'], $_POST['booked_itinerary']);
$connector->add_value("confirm_date", $_POST['confirm_date'], "s");
print $connector->get_content();