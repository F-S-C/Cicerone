<?php


namespace db_connector;

require_once "InsertConnector.php";

/**
 * Insert an itinerary in a user's wishlist.
 * @package db_connector
 */
class InsertWishlist extends InsertConnector
{
    protected const COLUMNS = "username, itinerary_in_wishlist";
    protected const COLUMNS_TYPE = "si";
    protected const TABLE_NAME = "wishlist";
}

$connector = new InsertWishlist();
$connector->add_value(array(strtolower($_POST['username']), $_POST['itinerary_in_wishlist']));
print $connector->get_content();