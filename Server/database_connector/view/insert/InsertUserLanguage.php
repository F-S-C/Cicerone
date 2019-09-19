<?php

namespace database_connector\view\insert;

use database_connector\controller\insert\InsertUserLanguage;

require_once "/membri/fsc/database_connector/controller/insert/InsertUserLanguage.php";

$connector = new InsertUserLanguage();
$connector->add_value(array(strtolower($_POST['username']), $_POST['language_code']));
print $connector->get_content();