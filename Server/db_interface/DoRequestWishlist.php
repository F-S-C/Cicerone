<?php

namespace db_interface;

use db_connector\RequestWishlist;

require_once "../db_connector/RequestWishlist.php";

$connector = new RequestWishlist($_POST['username'], $_POST['itinerary_in_wishlist']);
print $connector->get_content();