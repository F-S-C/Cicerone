<?php

namespace db_connector;

require_once("RequestReservation.php");

$connector = new RequestReservation($_POST['username'], $_POST['booked_itinerary']);
print $connector->get_content();