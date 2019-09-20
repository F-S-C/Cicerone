<?php

namespace database_connector\view\insert;

use database_connector\controller\insert\InsertReport;

require_once "/membri/fsc/database_connector/controller/insert/InsertReport.php";

$connector = new InsertReport();
$connector->add_value(array(strtolower(json_decode($_POST['username'], true)["username"]), strtolower(json_decode($_POST['reported_user'], true)["reported_user"])));
print $connector->get_content();