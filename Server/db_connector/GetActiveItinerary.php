<?php

namespace db_connector;

require_once("RequestActiveItinerary.php");

$connector = new RequestActiveItinerary($_POST['username'], $_POST['location'], $_POST['beginning_date'], $_POST['ending_date'], $_POST['itinerary_code']);
print $connector->get_content();