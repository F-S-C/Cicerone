<?php

namespace db_interface\delete;

use db_connector\delete\ClearWishlist;

require_once "../../db_connector/delete/ClearWishlist.php";

$connector = new ClearWishlist(strtolower($_POST['username']));
print $connector->get_content();