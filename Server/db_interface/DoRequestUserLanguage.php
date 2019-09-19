<?php

namespace db_interface;

use db_connector\RequestItineraryLanguage;

require_once("../db_connector/RequestUserLanguage.php");

$connector = new RequestItineraryLanguage($_POST['language_code'], $_POST['username']);
print $connector->get_content();