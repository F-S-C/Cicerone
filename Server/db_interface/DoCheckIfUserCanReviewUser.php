<?php

namespace db_interface;

use db_connector\CheckIfUserCanReviewUser;

require_once "../db_connector/CheckIfUserCanReviewUser.php";


$connector = new CheckIfUserCanReviewUser($_POST['username'], $_POST['reviewed_user']);
print $connector->get_content();