<?php

namespace database_connector\view\insert;

use database_connector\controller\insert\InsertDocument;

require_once "/membri/fsc/database_connector/controller/insert/InsertDocument.php";

$connector = new InsertDocument();
$connector->add_value(array(strtolower($_POST['username']),
    $_POST['document_number'],
    $_POST['document_type'],
    $_POST['expiry_date']
));
print $connector->get_content();