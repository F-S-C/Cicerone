<?php

namespace database_connector\view\insert;

use database_connector\controller\insert\InsertReportDetails;

require_once "../../controller/insert/InsertReportDetails.php";

$connector = new InsertReportDetails();
$connector->add_value(array($_POST['report_code'],
    $_POST['object'],
    trim($_POST['report_body']),
    $_POST['state']
));
print $connector->get_content();