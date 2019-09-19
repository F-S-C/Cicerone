<?php

namespace db_interface\insert;

use db_connector\insert\InsertUserLanguage;

require_once "../../db_connector/insert/InsertUserLanguage.php";

$connector = new InsertUserLanguage();
$connector->add_value(array(strtolower($_POST['username']), $_POST['language_code']));
print $connector->get_content();