<?php

namespace database_connector\view\delete;

use database_connector\controller\delete\DeleteReservation;

require_once "../../controller/delete/DeleteReservation.php";

$connector = new DeleteReservation($_POST['username'], $_POST['booked_itinerary']);
print $connector->get_content();
