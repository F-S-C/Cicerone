<?php

namespace database_connector\controller\insert;

require_once "/membri/fsc/database_connector/controller/insert/InsertConnector.php";

/**
 * Insert a report for the admin.
 * @package database_connector\controller\insert
 */
class InsertReport extends InsertConnector
{
    protected const COLUMNS = "username, reported_user";
    protected const COLUMNS_TYPE = "ss";
    protected const TABLE_NAME = "report";
}