<?php

namespace db_connector\delete;

use db_connector\DeleteConnector;

require_once "../DeleteConnector.php";

/**
 * A connector that deletes an itinerary.
 * @package db_connector
 */
class ClearWishlist extends DeleteConnector
{
    protected const TABLE_NAME = "wishlist";
    protected const ID_COLUMN = "username";
    protected const ID_COLUMN_TYPE = "s";
}