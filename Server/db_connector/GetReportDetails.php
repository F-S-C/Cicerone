<?php

namespace db_connector;

require_once("RequestReportDetails.php");

$connector = new RequestReportDetails($_POST['report_code']);
print $connector->get_content();