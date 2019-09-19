<?php

namespace database_connector\view\insert;

use database_connector\controller\insert\InsertItineraryLanguage;

require_once "../../controller/insert/InsertItineraryLanguage.php";

$connector = new InsertItineraryLanguage();
$connector->add_value(array($_POST['itinerary_code'], $_POST['language_code']));
print $connector->get_content();