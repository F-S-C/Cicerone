<?php

namespace db_interface\request;

use db_connector\request\RequestItineraryReview;

require_once "../../db_connector/request/RequestItineraryReview.php";

$connector = new RequestItineraryReview($_POST['username'], $_POST['reviewed_itinerary']);
print $connector->get_content();