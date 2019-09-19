<?php

namespace db_interface;

use db_connector\RequestReportDetails;

require_once "../db_connector/RequestReportDetails.php";

$connector = new RequestReportDetails($_POST['report_code']);
print $connector->get_content();