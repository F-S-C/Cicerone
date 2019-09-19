<?php

namespace db_interface\update;

use db_connector\update\UpdateUserReview;

require_once "../../db_connector/update/UpdateUserReview.php";

$connector = new UpdateUserReview($_POST['username'], $_POST['reviewed_user']);
$connector->add_value("feedback", $_POST['feedback'], "i");
$connector->add_value("description", $_POST['description'], "s");
print $connector->get_content();