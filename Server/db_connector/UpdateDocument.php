<?php

namespace db_connector;

require_once("UpdateConnector.php");

class UpdateDocument extends UpdateConnector
{
    protected const TABLE_NAME = "document";
    protected const ID_COLUMN = "username";
    protected const ID_COLUMN_TYPE = "s";
}

$connector = new UpdateDocument($_POST['username']);
$connector->add_value("document_number", $_POST['document_number'], "s");
$connector->add_value("document_type", $_POST['document_type'], "s");
$connector->add_value("expiry_date", $_POST['expiry_date'], "s");
print $connector->get_content();