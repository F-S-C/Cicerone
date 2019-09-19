<?php

namespace db_interface\insert;

use db_connector\insert\InsertReportDetails;

require_once "../../db_connector/insert/InsertReportDetails.php";

$connector = new InsertReportDetails();
$connector->add_value(array($_POST['report_code'],
    $_POST['object'],
    trim($_POST['report_body']),
    $_POST['state']
));
print $connector->get_content();