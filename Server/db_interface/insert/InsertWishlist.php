<?php

namespace db_interface\insert;

use db_connector\insert\InsertWishlist;

require_once "../../db_connector/insert/InsertWishlist.php";

$connector = new InsertWishlist();
$connector->add_value(array(strtolower($_POST['username']), $_POST['itinerary_in_wishlist']));
print $connector->get_content();