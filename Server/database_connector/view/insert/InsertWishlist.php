<?php

namespace database_connector\view\insert;

use database_connector\controller\insert\InsertWishlist;

require_once "/home/fsc/www/database_connector/controller/insert/InsertWishlist.php";

$connector = new InsertWishlist();
$connector->add_value(array(strtolower($_POST['username']), $_POST['itinerary_in_wishlist']));
print $connector->get_content();