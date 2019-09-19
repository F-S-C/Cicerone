<?php

namespace database_connector\view\delete;

use database_connector\controller\delete\DeleteItinerary;

require_once "/membri/fsc/database_connector/controller/delete/DeleteItinerary.php";

$connector = new DeleteItinerary($_POST['itinerary_code']);
print $connector->get_content();