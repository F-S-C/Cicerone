<?php

namespace database_connector\view\delete;

use database_connector\controller\delete\DeleteUserReview;

require_once "/home/fsc/www/database_connector/controller/delete/DeleteUserReview.php";

$connector = new DeleteUserReview($_POST['username'], $_POST['reviewed_user']);
print $connector->get_content();
