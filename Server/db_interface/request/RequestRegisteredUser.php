<?php

namespace db_interface\request;

use db_connector\request\RequestRegisteredUser;

require_once "../../db_connector/request/RequestRegisteredUser.php";

$connector = new RequestRegisteredUser($_POST['username'], $_POST['email']);
print $connector->get_content();