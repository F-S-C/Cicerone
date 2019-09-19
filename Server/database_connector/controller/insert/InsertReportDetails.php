<?php

namespace database_connector\controller\insert;

require_once "InsertConnector.php";

/**
 * Insert the details for a report.
 * @package database_connector\controller\insert
 */
class InsertReportDetails extends InsertConnector
{
    protected const COLUMNS = "report_code, object, report_body, state";
    protected const COLUMNS_TYPE = "issi";
    protected const TABLE_NAME = "report_details";
}