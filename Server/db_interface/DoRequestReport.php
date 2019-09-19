<?php

namespace db_interface;

use db_connector\RequestReport;

require_once "../db_connector/RequestReport.php";

$connector = new RequestReport($_POST['report_code'], $_POST['reported_user'], $_POST['username']);
print $connector->get_content();