<?php

namespace db_interface\request;

use db_connector\request\RequestItineraryLanguage;

require_once "../../db_connector/request/RequestItineraryLanguage.php";

$connector = new RequestItineraryLanguage($_POST['language_code'], $_POST['itinerary_code']);
print $connector->get_content();