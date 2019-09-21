<?php

namespace database_connector\view\insert;

use database_connector\controller\insert\InsertReport;

require_once "/membri/fsc/database_connector/controller/insert/InsertReport.php";

$connector = new InsertReport();
$connector->add_value(array(
    strtolower($_POST['username']),
    strtolower($_POST['reported_user']),
    trim($_POST['object']),
    trim($_POST['report_body']),
    $_POST['state']
));
print $connector->get_content();