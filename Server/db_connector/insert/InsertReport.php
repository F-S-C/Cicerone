<?php

namespace db_connector\insert;

require_once "InsertConnector.php";

/**
 * Insert a report for the admin.
 * @package db_connector
 */
class InsertReport extends InsertConnector
{
    protected const COLUMNS = "username, reported_user";
    protected const COLUMNS_TYPE = "ss";
    protected const TABLE_NAME = "report";
}