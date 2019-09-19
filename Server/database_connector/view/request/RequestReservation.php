<?php

namespace database_connector\view\request;

use database_connector\controller\request\RequestReservation;

require_once "../../controller/request/RequestReservation.php";

$connector = new RequestReservation($_POST['username'], $_POST['booked_itinerary']);
print $connector->get_content();