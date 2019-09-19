<?php

namespace database_connector\view\request;

use database_connector\controller\request\RequestRegisteredUser;

require_once "/membri/fsc/database_connector/controller/request/RequestRegisteredUser.php";

$connector = new RequestRegisteredUser($_POST['username'], $_POST['email']);
print $connector->get_content();