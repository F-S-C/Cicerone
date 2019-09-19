<?php

namespace database_connector\view\request;

use database_connector\controller\request\RequestReport;

require_once "/membri/fsc/database_connector/controller/request/RequestReport.php";

$connector = new RequestReport($_POST['report_code'], $_POST['reported_user'], $_POST['username']);
print $connector->get_content();