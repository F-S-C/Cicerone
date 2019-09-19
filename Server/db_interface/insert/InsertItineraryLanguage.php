<?php

namespace db_interface\insert;

use db_connector\insert\InsertItineraryLanguage;

require_once "../../db_connector/insert/InsertItineraryLanguage.php";

$connector = new InsertItineraryLanguage();
$connector->add_value(array($_POST['itinerary_code'], $_POST['language_code']));
print $connector->get_content();