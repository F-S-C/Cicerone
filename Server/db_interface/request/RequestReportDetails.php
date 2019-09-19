<?php

namespace db_interface\request;

use db_connector\request\RequestReportDetails;

require_once "../../db_connector/request/RequestReportDetails.php";

$connector = new RequestReportDetails($_POST['report_code']);
print $connector->get_content();