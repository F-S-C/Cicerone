<?php

namespace db_connector;

require_once("RequestWishlist.php");

$connector = new RequestWishlist($_POST['username'], $_POST['itinerary_in_wishlist']);
print $connector->get_content();