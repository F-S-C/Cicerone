<?php

namespace db_connector;

require_once("RequestRegisteredUser.php");

$connector = new RequestRegisteredUser($_POST['username'], $_POST['email']);
print $connector->get_content();