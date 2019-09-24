<?php


namespace database_connector\view\delete;

use database_connector\controller\delete\DeleteRegisteredUser;

require_once "/home/fsc/www/database_connector/controller/delete/DeleteRegisteredUser.php";


$connector = new DeleteRegisteredUser($_POST['username']);
print $connector->get_content();