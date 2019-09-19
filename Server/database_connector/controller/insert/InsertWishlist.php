<?php

namespace database_connector\controller\insert;

require_once "InsertConnector.php";

/**
 * Insert an itinerary in a user's wishlist.
 * @package database_connector\controller\insert
 */
class InsertWishlist extends InsertConnector
{
    protected const COLUMNS = "username, itinerary_in_wishlist";
    protected const COLUMNS_TYPE = "si";
    protected const TABLE_NAME = "wishlist";
}