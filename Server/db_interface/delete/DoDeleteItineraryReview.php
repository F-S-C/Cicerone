<?php

namespace db_interface\delete;

use db_connector\delete\DeleteItineraryReview;

require_once "../../db_connector/delete/DeleteItineraryReview.php";

$connector = new DeleteItineraryReview($_POST['username'], $_POST['reviewed_itinerary']);
print $connector->get_content();
