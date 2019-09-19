<?php

namespace database_connector\view\delete;

use database_connector\controller\delete\DeleteItineraryReview;

require_once "../../controller/delete/DeleteItineraryReview.php";

$connector = new DeleteItineraryReview($_POST['username'], $_POST['reviewed_itinerary']);
print $connector->get_content();
