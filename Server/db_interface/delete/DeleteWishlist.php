<?php

namespace db_interface\delete;

use db_connector\delete\DeleteWishlist;

require_once "../../db_connector/delete/DeleteWishlist.php";

$connector = new DeleteWishlist($_POST['username'], $_POST['itinerary_in_wishlist']);
print $connector->get_content();
