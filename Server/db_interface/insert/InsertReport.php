<?php

namespace db_interface\insert;

use db_connector\insert\InsertReport;

require_once "../../db_connector/insert/InsertReport.php";

$connector = new InsertReport();
$connector->add_value(array(strtolower($_POST['username']), strtolower($_POST['reported_user'])));
print $connector->get_content();