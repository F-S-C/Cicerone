<?php

namespace db_connector\insert;

require_once "InsertConnector.php";

/**
 * Insert a document.
 * @package db_connector
 */
class InsertDocument extends InsertConnector
{
    protected const COLUMNS = "username, document_number, document_type, expiry_date";
    protected const COLUMNS_TYPE = "ssss";
    protected const TABLE_NAME = "document";
}