<?php

namespace db_interface\request;

use db_connector\request\RequestItineraryLanguage;

require_once "../../db_connector/request/RequestUserLanguage.php";

$connector = new RequestItineraryLanguage($_POST['language_code'], $_POST['username']);
print $connector->get_content();