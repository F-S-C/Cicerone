<?php

namespace db_interface;

use db_connector\RequestItineraryJoinReservation;

require_once("../db_connector/RequestItineraryJoinReservation.php");

$connector = new RequestItineraryJoinReservation($_POST['username'], $_POST['cicerone']);
print $connector->get_content();