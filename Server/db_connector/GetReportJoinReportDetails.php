<?php

namespace db_connector;

require_once("RequestReportJoinReportDetails.php");

$connector = new RequestReportJoinReportDetails($_POST['report_code'], $_POST['reported_user'], $_POST['username']);
print $connector->get_content();