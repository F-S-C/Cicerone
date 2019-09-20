<?php

namespace database_connector\controller\update;

require_once "/membri/fsc/database_connector/controller/update/UpdateConnector.php";

/**
 * Update details of the report.
 */
class UpdateReportDetails extends UpdateConnector
{
    protected const TABLE_NAME = "report_details";
    protected const ID_COLUMN = "report_code";
    protected const ID_COLUMN_TYPE = "i";
}