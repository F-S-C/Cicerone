<?php

namespace database_connector\controller\insert;

require_once "/membri/fsc/database_connector/controller/insert/InsertConnector.php";

/**
 * Insert an itinerary in a user's wishlist.
 */
class InsertWishlist extends InsertConnector
{
    protected const COLUMNS = "username, itinerary_in_wishlist";
    protected const COLUMNS_TYPE = "si";
    protected const TABLE_NAME = "wishlist";
}