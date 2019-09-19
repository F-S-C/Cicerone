<?php

namespace database_connector\controller\delete;

use database_connector\controller\DeleteConnector;

require_once "../DeleteConnector.php";

/**
 * A connector that deletes an itinerary.
 * @package database_connector\controller\delete
 */
class ClearWishlist extends DeleteConnector
{
    protected const TABLE_NAME = "wishlist";
    protected const ID_COLUMN = "username";
    protected const ID_COLUMN_TYPE = "s";
}