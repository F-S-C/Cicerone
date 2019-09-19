<?php

namespace db_interface\update;

use db_connector\update\UpdateDocument;

require_once "../../db_connector/update/UpdateDocument.php";

$connector = new UpdateDocument($_POST['username']);
$connector->add_value("document_number", $_POST['document_number'], "s");
$connector->add_value("document_type", $_POST['document_type'], "s");
$connector->add_value("expiry_date", $_POST['expiry_date'], "s");
print $connector->get_content();