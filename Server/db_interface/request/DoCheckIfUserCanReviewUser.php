<?php

namespace db_interface\request;

use db_connector\request\CheckIfUserCanReviewUser;

require_once "../../db_connector/request/CheckIfUserCanReviewUser.php";


$connector = new CheckIfUserCanReviewUser($_POST['username'], $_POST['reviewed_user']);
print $connector->get_content();