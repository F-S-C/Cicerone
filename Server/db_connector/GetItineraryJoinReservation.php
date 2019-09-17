<?php

namespace db_connector;

require_once("RequestItineraryJoinReservation.php");

$connector = new RequestItineraryJoinReservation($_POST['username'], $_POST['cicerone']);
print $connector->get_content();