<?php

namespace database_connector\view\request;

use database_connector\controller\request\RequestReportDetails;

require_once "/membri/fsc/database_connector/controller/request/RequestReportDetails.php";

$connector = new RequestReportDetails($_POST['report_code']);
print $connector->get_content();