<?php

namespace db_connector;

require_once("RequestItineraryLanguage.php");

$connector = new RequestItineraryLanguage($_POST['language_code'], $_POST['itinerary_code']);
print $connector->get_content();