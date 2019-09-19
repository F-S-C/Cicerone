<?php

namespace db_interface;

use db_connector\RequestItineraryLanguage;

require_once("../db_connector/RequestItineraryLanguage.php");

$connector = new RequestItineraryLanguage($_POST['language_code'], $_POST['itinerary_code']);
print $connector->get_content();