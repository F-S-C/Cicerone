<?php

namespace db_interface\delete;

use db_connector\delete\DeleteItinerary;

require_once "../../db_connector/delete/DeleteItinerary.php";

$connector = new DeleteItinerary($_POST['itinerary_code']);
print $connector->get_content();