<?php

namespace database_connector\view\request;

use database_connector\controller\request\RequestWishlistJoinItinerary;

require_once "../../controller/request/RequestWishlistJoinItinerary.php";

$connector = new RequestWishlistJoinItinerary($_POST['username']);
print $connector->get_content();