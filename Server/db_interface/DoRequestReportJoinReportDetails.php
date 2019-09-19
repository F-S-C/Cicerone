<?php

namespace db_interface;

use db_connector\RequestReportJoinReportDetails;

require_once("../db_connector/RequestReportJoinReportDetails.php");

$connector = new RequestReportJoinReportDetails($_POST['report_code'], $_POST['reported_user'], $_POST['username']);
print $connector->get_content();