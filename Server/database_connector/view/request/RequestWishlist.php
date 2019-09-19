<?php

namespace database_connector\view\request;

use database_connector\controller\request\RequestWishlist;

require_once "/membri/fsc/database_connector/controller/request/RequestWishlist.php";

$connector = new RequestWishlist($_POST['username'], $_POST['itinerary_in_wishlist']);
print $connector->get_content();