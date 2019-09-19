<?php

namespace database_connector\view\update;

use database_connector\controller\update\UpdateItineraryReview;

require_once "/membri/fsc/database_connector/controller/update/UpdateItineraryReview.php";

$connector = new UpdateItineraryReview($_POST['username'], $_POST['reviewed_itinerary']);
$connector->add_value("feedback", $_POST['feedback'], "i");
$connector->add_value("description", $_POST['description'], "s");
print $connector->get_content();