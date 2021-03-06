<?php

namespace database_connector\controller\insert;

require_once "/home/fsc/www/database_connector/controller/insert/InsertConnector.php";

/**
 * Insert a document.
 */
class InsertDocument extends InsertConnector
{
    protected const COLUMNS = "username, document_number, document_type, expiry_date";
    protected const COLUMNS_TYPE = "ssss";
    protected const TABLE_NAME = "document";
}