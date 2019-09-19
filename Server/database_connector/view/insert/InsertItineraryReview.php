<?php

namespace database_connector\view\insert;

use database_connector\controller\insert\InsertItineraryReview;

require_once "/membri/fsc/database_connector/controller/insert/InsertItineraryReview.php";

$connector = new InsertItineraryReview();
$connector->add_value(array(strtolower($_POST['username']),
    $_POST['reviewed_itinerary'],
    $_POST['feedback'],
    $_POST['description']
));
print $connector->get_content();