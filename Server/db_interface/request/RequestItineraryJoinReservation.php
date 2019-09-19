<?php

namespace db_interface\request;

use db_connector\request\RequestItineraryJoinReservation;

require_once "../../db_connector/request/RequestItineraryJoinReservation.php";

$connector = new RequestItineraryJoinReservation($_POST['username'], $_POST['cicerone']);
print $connector->get_content();