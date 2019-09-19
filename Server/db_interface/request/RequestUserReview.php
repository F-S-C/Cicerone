<?php

namespace db_interface\request;

use db_connector\request\RequestUserReview;

require_once "../../db_connector/request/RequestUserReview.php";

$connector = new RequestUserReview($_POST['username'], $_POST['reviewed_user']);
print $connector->get_content();