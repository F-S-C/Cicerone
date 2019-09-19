<?php


namespace db_interface\delete;

use db_connector\delete\DeleteRegisteredUser;

require_once "../../db_connector/delete/DeleteRegisteredUser.php";


$connector = new DeleteRegisteredUser($_POST['username']);
print $connector->get_content();