<?php

namespace db_interface\update;

use db_connector\update\UpdateReportDetails;

require_once "../../db_connector/update/UpdateReportDetails.php";

$connector = new UpdateReportDetails($_POST['report_code']);
$connector->add_value("state", $_POST['state'], "i");
$connector->add_value("report_body", $_POST['report_body'], "s");
$connector->add_value("object", $_POST['object'], "s");
print $connector->get_content();