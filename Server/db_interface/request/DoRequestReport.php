<?php

namespace db_interface\request;

use db_connector\request\RequestReport;

require_once "../../db_connector/request/RequestReport.php";

$connector = new RequestReport($_POST['report_code'], $_POST['reported_user'], $_POST['username']);
print $connector->get_content();