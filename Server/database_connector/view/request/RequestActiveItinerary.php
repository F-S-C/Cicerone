<?php

namespace database_connector\view\request;

use database_connector\controller\request\RequestActiveItinerary;

require_once "../../controller/request/RequestActiveItinerary.php";

$connector = new RequestActiveItinerary($_POST['username'], $_POST['location'], $_POST['beginning_date'], $_POST['ending_date'], $_POST['itinerary_code']);
print $connector->get_content();