<?php

namespace db_connector;

require_once "UpdateConnector.php";

/**
 * Update details of the report.
 * @package db_connector
 */
class UpdateReportDetails extends UpdateConnector
{
    protected const TABLE_NAME = "report_details";
    protected const ID_COLUMN = "report_code";
    protected const ID_COLUMN_TYPE = "i";
}

$connector = new UpdateReportDetails($_POST['report_code']);
$connector->add_value("state", $_POST['state'], "i");
$connector->add_value("report_body", $_POST['report_body'], "s");
$connector->add_value("object", $_POST['object'], "s");
print $connector->get_content();