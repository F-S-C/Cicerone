<?php

namespace db_interface\request;

use db_connector\request\RequestWishlist;

require_once "../../db_connector/request/RequestWishlist.php";

$connector = new RequestWishlist($_POST['username'], $_POST['itinerary_in_wishlist']);
print $connector->get_content();