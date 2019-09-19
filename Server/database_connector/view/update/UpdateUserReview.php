<?php

namespace database_connector\view\update;

use database_connector\controller\update\UpdateUserReview;

require_once "/membri/fsc/database_connector/controller/update/UpdateUserReview.php";

$connector = new UpdateUserReview($_POST['username'], $_POST['reviewed_user']);
$connector->add_value("feedback", $_POST['feedback'], "i");
$connector->add_value("description", $_POST['description'], "s");
print $connector->get_content();