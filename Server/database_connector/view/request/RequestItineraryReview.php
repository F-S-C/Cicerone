<?php

namespace database_connector\view\request;

use database_connector\controller\request\RequestItineraryReview;

require_once "/home/fsc/www/database_connector/controller/request/RequestItineraryReview.php";

$connector = new RequestItineraryReview($_POST['username'], $_POST['reviewed_itinerary']);
print $connector->get_content();