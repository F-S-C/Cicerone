<?php

namespace db_interface;

use db_connector\RequestItineraryReview;

require_once "../db_connector/RequestItineraryReview.php";

$connector = new RequestItineraryReview($_POST['username'], $_POST['reviewed_itinerary']);
print $connector->get_content();