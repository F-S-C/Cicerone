<?php

namespace db_connector;

require_once("RequestItineraryReview.php");

$connector = new RequestItineraryReview($_POST['username'], $_POST['reviewed_itinerary']);
print $connector->get_content();