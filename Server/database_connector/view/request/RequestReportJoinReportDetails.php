<?php

namespace database_connector\view\request;

use database_connector\controller\request\RequestReportJoinReportDetails;

require_once "../../controller/request/RequestReportJoinReportDetails.php";

$connector = new RequestReportJoinReportDetails($_POST['report_code'], $_POST['reported_user'], $_POST['username']);
print $connector->get_content();