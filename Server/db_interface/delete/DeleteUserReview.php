<?php

namespace db_interface\delete;

use db_connector\delete\DeleteUserReview;

require_once "../../db_connector/delete/DeleteUserReview.php";

$connector = new DeleteUserReview($_POST['username'], $_POST['reviewed_user']);
print $connector->get_content();
