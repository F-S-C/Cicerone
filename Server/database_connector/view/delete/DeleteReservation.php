<?php

namespace database_connector\view\delete;

use database_connector\controller\delete\DeleteReservation;

require_once "/membri/fsc/database_connector/controller/delete/DeleteReservation.php";

$connector = new DeleteReservation(json_decode($_POST['username'], true)['username'], json_decode($_POST['booked_itinerary'], true)['booked_itinerary']);
print $connector->get_content();
