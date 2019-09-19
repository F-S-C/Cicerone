<?php

namespace database_connector\view\request;

use database_connector\controller\request\RequestItineraryLanguage;

require_once "../../controller/request/RequestUserLanguage.php";

$connector = new RequestItineraryLanguage($_POST['language_code'], $_POST['username']);
print $connector->get_content();