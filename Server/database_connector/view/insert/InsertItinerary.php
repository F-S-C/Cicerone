<?php

namespace database_connector\view\insert;

use database_connector\controller\insert\InsertItinerary;

require_once "/membri/fsc/database_connector/controller/insert/InsertItinerary.php";

$connector = new InsertItinerary();
$connector->add_value(array($_POST['title'],
    $_POST['description'],
    $_POST['beginning_date'],
    $_POST['ending_date'],
    $_POST['end_reservations_date'],
    $_POST['maximum_participants_number'],
    $_POST['minimum_participants_number'],
    $_POST['location'],
    $_POST['repetitions_per_day'],
    $_POST['duration'],
    strtolower(json_decode($_POST['username'], true)["username"]),
    $_POST['full_price'],
    $_POST['reduced_price'],
    $_POST['image_url']
));
print $connector->get_content();