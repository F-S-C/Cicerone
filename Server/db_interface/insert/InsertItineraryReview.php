<?php

namespace db_interface\insert;

use db_connector\insert\InsertItineraryReview;

require_once "../../db_connector/insert/InsertItineraryReview.php";

$connector = new InsertItineraryReview();
$connector->add_value(array(strtolower($_POST['username']),
    $_POST['reviewed_itinerary'],
    $_POST['feedback'],
    $_POST['description']
));
print $connector->get_content();