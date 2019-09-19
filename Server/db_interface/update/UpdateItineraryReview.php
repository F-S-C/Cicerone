<?php

namespace db_interface\update;

use db_connector\update\UpdateItineraryReview;

require_once "../../db_connector/update/UpdateItineraryReview.php";

$connector = new UpdateItineraryReview($_POST['username'], $_POST['reviewed_itinerary']);
$connector->add_value("feedback", $_POST['feedback'], "i");
$connector->add_value("description", $_POST['description'], "s");
print $connector->get_content();