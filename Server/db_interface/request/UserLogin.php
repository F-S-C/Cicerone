<?php

namespace db_interface\request;

use db_connector\request\UserLogin;

require_once "../../db_connector/request/UserLogin.php";

$connector = new UserLogin($_POST['username'], $_POST['password']);
print $connector->get_content();