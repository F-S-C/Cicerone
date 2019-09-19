<?php

namespace db_interface;

use db_connector\RequestUserReview;

require_once("../db_connector/RequestUserReview.php");

$connector = new RequestUserReview($_POST['username'], $_POST['reviewed_user']);
print $connector->get_content();