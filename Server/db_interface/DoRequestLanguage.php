<?php

namespace db_interface;

use db_connector\RequestLanguage;

require_once "../db_connector/RequestLanguage.php";

$connector = new RequestLanguage($_POST['language_code']);
print $connector->get_content();