<?php

namespace database_connector\view\request;

use database_connector\controller\request\RequestItineraryJoinReservation;

require_once "../../controller/request/RequestItineraryJoinReservation.php";

$connector = new RequestItineraryJoinReservation($_POST['username'], $_POST['cicerone']);
print $connector->get_content();