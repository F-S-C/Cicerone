<?php

namespace database_connector\view\request;

use database_connector\controller\request\CheckIfUserCanReviewUser;

require_once "/membri/fsc/database_connector/controller/request/CheckIfUserCanReviewUser.php";


$connector = new CheckIfUserCanReviewUser($_GET['username'], $_GET['reviewed_user']);
print $connector->get_content();