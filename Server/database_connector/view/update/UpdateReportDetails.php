<?php

namespace database_connector\view\update;

use database_connector\controller\update\UpdateReportDetails;

require_once "/home/fsc/www/database_connector/controller/update/UpdateReportDetails.php";

$connector = new UpdateReportDetails($_POST['report_code']);
$connector->add_value("state", $_POST['state'], "i");
$connector->add_value("report_body", $_POST['report_body'], "s");
$connector->add_value("object", $_POST['object'], "s");
print $connector->get_content();