<?php

namespace database_connector\view\delete;

use database_connector\controller\delete\DeleteWishlist;

require_once "/membri/fsc/database_connector/controller/delete/DeleteWishlist.php";

$connector = new DeleteWishlist($_POST['username'], $_POST['itinerary_in_wishlist']);
print $connector->get_content();
