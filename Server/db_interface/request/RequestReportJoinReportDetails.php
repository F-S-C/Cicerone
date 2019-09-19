<?php

namespace db_interface\request;

use db_connector\request\RequestReportJoinReportDetails;

require_once "../../db_connector/request/RequestReportJoinReportDetails.php";

$connector = new RequestReportJoinReportDetails($_POST['report_code'], $_POST['reported_user'], $_POST['username']);
print $connector->get_content();