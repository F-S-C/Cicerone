<?php

namespace db_interface\request;

use db_connector\request\RequestReservation;

require_once "../../db_connector/request/RequestReservation.php";

$connector = new RequestReservation($_POST['username'], $_POST['booked_itinerary']);
print $connector->get_content();