<?php

namespace db_interface\request;

use db_connector\request\RequestLanguage;

require_once "../../db_connector/request/RequestLanguage.php";

$connector = new RequestLanguage($_POST['language_code']);
print $connector->get_content();