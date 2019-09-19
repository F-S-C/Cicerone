<?php

namespace db_interface\insert;

use db_connector\insert\InsertUserReview;

require_once "../../db_connector/insert/InsertUserReview.php";

$connector = new InsertUserReview();
$connector->add_value(array(strtolower($_POST['username']), strtolower($_POST['reviewed_user']), $_POST['feedback'], $_POST['description']));
print $connector->get_content();