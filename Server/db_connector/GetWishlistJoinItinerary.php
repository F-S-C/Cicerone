<?php

namespace db_connector;

require_once("RequestWishlistJoinItinerary.php");

$connector = new RequestWishlistJoinItinerary($_POST['username']);
print $connector->get_content();