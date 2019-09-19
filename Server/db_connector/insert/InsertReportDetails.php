<?php

namespace db_connector\insert;

require_once "InsertConnector.php";

/**
 * Insert the details for a report.
 * @package db_connector
 */
class InsertReportDetails extends InsertConnector
{
    protected const COLUMNS = "report_code, object, report_body, state";
    protected const COLUMNS_TYPE = "issi";
    protected const TABLE_NAME = "report_details";
}