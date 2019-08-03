<?php

namespace db_connector;

require_once("InsertConnector.php");

class InsertReportDetails extends InsertConnector
{
    protected const COLUMNS = "report_code, object, report_body, state";
    protected const COLUMNS_TYPE = "issi";
    protected const TABLE_NAME = "report_details";
}

$connector = new InsertReportDetails();
$connector->add_value(array($_POST['report_code'],
    $_POST['object'],
    $_POST['report_body'],
    $_POST['state']
));
print $connector->get_content();