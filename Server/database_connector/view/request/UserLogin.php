<?php

namespace database_connector\view\request;

use database_connector\controller\request\UserLogin;

require_once "/home/fsc/www/database_connector/controller/request/UserLogin.php";

$connector = new UserLogin($_POST['username'], $_POST['password']);
print $connector->get_content();