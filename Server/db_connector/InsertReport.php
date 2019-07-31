<?php

namespace db_connector;

require_once("InsertConnector.php");

class InsertReport extends InsertConnector
{
    protected const COLUMNS = "username, reported_user";
    protected const COLUMNS_TYPE = "ss";
    protected const TABLE_NAME = "report";
}

$connector = new InsertReport();
$connector->add_value(array(strtolower($_POST['username']), strtolower($_POST['report_user'])));
print $connector->get_content();