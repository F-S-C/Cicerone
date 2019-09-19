<?php

namespace database_connector\view\update;

use database_connector\controller\update\UpdateItinerary;

require_once "../../controller/update/UpdateItinerary.php";

$connector = new UpdateItinerary($_POST['itinerary_code']);
$connector->add_value("title", $_POST['title'], "s");
$connector->add_value("description", $_POST['description'], "s");
$connector->add_value("beginning_date", $_POST['beginning_date'], "s");
$connector->add_value("ending_date", $_POST['ending_date'], "s");
$connector->add_value("end_reservations_date", $_POST['end_reservations_date'], "s");
$connector->add_value("maximum_participants_number", $_POST['maximum_participants_number'], "i");
$connector->add_value("minimum_participants_number", $_POST['minimum_participants_number'], "i");
$connector->add_value("location", $_POST['location'], "s");
$connector->add_value("repetitions_per_day", $_POST['repetitions_per_day'], "i");
$connector->add_value("duration", $_POST['duration'], "s");
$connector->add_value("full_price", $_POST['full_price'], "d");
$connector->add_value("reduced_price", $_POST['reduced_price'], "d");
print $connector->get_content();