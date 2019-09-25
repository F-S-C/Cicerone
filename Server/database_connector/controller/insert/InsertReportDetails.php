<?php

namespace database_connector\controller\insert;

require_once "/home/fsc/www/database_connector/controller/insert/InsertConnector.php";

/**
 * Insert the details for a report.
 */
class InsertReportDetails extends InsertConnector
{
    protected const COLUMNS = "report_code, object, report_body, state";
    protected const COLUMNS_TYPE = "issi";
    protected const TABLE_NAME = "report_details";
}