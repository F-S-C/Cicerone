<?php

namespace db_interface\request;

use db_connector\request\RequestDocument;

require_once "../../db_connector/request/RequestDocument.php";

$connector = new RequestDocument($_POST['username']);
print $connector->get_content();