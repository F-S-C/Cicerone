<?php

namespace database_connector\view\request;

use database_connector\controller\request\RequestDocument;

require_once "../../controller/request/RequestDocument.php";

$connector = new RequestDocument($_POST['username']);
print $connector->get_content();