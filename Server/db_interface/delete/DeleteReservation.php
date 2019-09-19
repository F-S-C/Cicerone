<?php

namespace db_interface\delete;

use db_connector\delete\DeleteReservation;

require_once "../../db_connector/delete/DeleteReservation.php";

$connector = new DeleteReservation($_POST['username'], $_POST['booked_itinerary']);
print $connector->get_content();
