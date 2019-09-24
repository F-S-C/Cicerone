<?php

namespace database_connector\view\request;

use database_connector\controller\request\RequestLanguage;

require_once "/home/fsc/www/database_connector/controller/request/RequestLanguage.php";

$connector = new RequestLanguage($_POST['language_code']);
print $connector->get_content();