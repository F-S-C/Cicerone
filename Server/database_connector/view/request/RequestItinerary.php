<?php

namespace database_connector\view\request;

use database_connector\controller\request\RequestItinerary;

require_once "../../controller/request/RequestItinerary.php";

$connector = new RequestItinerary($_POST['username'], $_POST['location'], $_POST['beginning_date'], $_POST['ending_date'], $_POST['itinerary_code']);
print $connector->get_content();