<?php

namespace db_interface\insert;

use db_connector\insert\InsertLanguage;

require_once "../../db_connector/insert/InsertLanguage.php";

$connector = new InsertLanguage();
$connector->add_value(array($_POST['language_code'], $_POST['language_name']));
print $connector->get_content();