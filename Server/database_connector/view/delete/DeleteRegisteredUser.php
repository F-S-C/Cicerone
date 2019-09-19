<?php


namespace database_connector\view\delete;

use database_connector\controller\delete\DeleteRegisteredUser;

require_once "../../controller/delete/DeleteRegisteredUser.php";


$connector = new DeleteRegisteredUser($_POST['username']);
print $connector->get_content();