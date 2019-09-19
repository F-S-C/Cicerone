<?php

namespace db_interface\request;

use db_connector\request\RequestWishlistJoinItinerary;

require_once "../../db_connector/request/RequestWishlistJoinItinerary.php";

$connector = new RequestWishlistJoinItinerary($_POST['username']);
print $connector->get_content();