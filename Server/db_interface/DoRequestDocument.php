<?php

namespace db_interface;

use db_connector\RequestDocument;

require_once "../db_connector/RequestDocument.php";

$connector = new RequestDocument($_POST['username']);
print $connector->get_content();