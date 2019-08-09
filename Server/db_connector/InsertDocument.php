<?php


namespace db_connector;

require_once("InsertConnector.php");

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

$connector = new InsertDocument();
$connector->add_value(array(strtolower($_POST['username']),
    $_POST['document_number'],
    $_POST['document_type'],
    $_POST['expiry_date']
));
print $connector->get_content();