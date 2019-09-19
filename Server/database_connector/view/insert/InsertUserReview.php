<?php

namespace database_connector\view\insert;

use database_connector\controller\insert\InsertUserReview;

require_once "/membri/fsc/database_connector/controller/insert/InsertUserReview.php";

$connector = new InsertUserReview();
$connector->add_value(array(strtolower($_POST['username']), strtolower($_POST['reviewed_user']), $_POST['feedback'], $_POST['description']));
print $connector->get_content();