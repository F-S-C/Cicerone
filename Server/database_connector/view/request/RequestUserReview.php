<?php

namespace database_connector\view\request;

use database_connector\controller\request\RequestUserReview;

require_once "/home/fsc/www/database_connector/controller/request/RequestUserReview.php";

$connector = new RequestUserReview($_POST['username'], $_POST['reviewed_user']);
print $connector->get_content();