<?php

namespace db_connector;

require_once("RequestUserReview.php");

$connector = new RequestUserReview($_POST['username'], $_POST['reviewed_user']);
print $connector->get_content();