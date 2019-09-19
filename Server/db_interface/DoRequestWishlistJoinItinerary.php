<?php

namespace db_interface;

use db_connector\RequestWishlistJoinItinerary;

require_once "../db_connector/RequestWishlistJoinItinerary.php";

$connector = new RequestWishlistJoinItinerary($_POST['username']);
print $connector->get_content();