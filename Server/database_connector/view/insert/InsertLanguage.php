<?php

namespace database_connector\view\insert;

use database_connector\controller\insert\InsertLanguage;

require_once "/home/fsc/www/database_connector/controller/insert/InsertLanguage.php";

$connector = new InsertLanguage();
$connector->add_value(array($_POST['language_code'], $_POST['language_name']));
print $connector->get_content();