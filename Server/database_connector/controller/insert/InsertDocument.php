<?php

namespace database_connector\controller\insert;

require_once "/membri/fsc/database_connector/controller/insert/InsertConnector.php";

/**
 * Insert a document.
 * @package database_connector\controller\insert
 */
class InsertDocument extends InsertConnector
{
    protected const COLUMNS = "username, document_number, document_type, expiry_date";
    protected const COLUMNS_TYPE = "ssss";
    protected const TABLE_NAME = "document";
}