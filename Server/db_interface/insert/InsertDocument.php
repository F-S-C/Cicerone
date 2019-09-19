<?php

namespace db_interface\insert;

use db_connector\insert\InsertDocument;

require_once "../../db_connector/insert/InsertDocument.php";

$connector = new InsertDocument();
$connector->add_value(array(strtolower($_POST['username']),
    $_POST['document_number'],
    $_POST['document_type'],
    $_POST['expiry_date']
));
print $connector->get_content();