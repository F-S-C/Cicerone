<?php

namespace db_interface;

use db_connector\RequestReservation;

require_once("../db_connector/RequestReservation.php");

$connector = new RequestReservation($_POST['username'], $_POST['booked_itinerary']);
print $connector->get_content();