<?php

namespace db_interface;

use db_connector\RequestRegisteredUser;

require_once("../db_connector/RequestRegisteredUser.php");

$connector = new RequestRegisteredUser($_POST['username'], $_POST['email']);
print $connector->get_content();