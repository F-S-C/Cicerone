<?php

namespace database_connector\view\request;

use database_connector\controller\request\CheckIfUserCanReviewUser;

require_once "/home/fsc/www/database_connector/controller/request/CheckIfUserCanReviewUser.php";


$connector = new CheckIfUserCanReviewUser($_POST['username'], $_POST['reviewed_user']);
print $connector->get_content();