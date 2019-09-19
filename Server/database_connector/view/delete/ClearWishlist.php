<?php

namespace database_connector\view\delete;

use database_connector\controller\delete\ClearWishlist;

require_once "/membri/fsc/database_connector/controller/delete/ClearWishlist.php";

$connector = new ClearWishlist(strtolower($_POST['username']));
print $connector->get_content();