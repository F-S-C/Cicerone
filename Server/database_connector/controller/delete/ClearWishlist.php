<?php

namespace database_connector\controller\delete;

require_once "/home/fsc/www/database_connector/controller/delete/DeleteConnector.php";

/**
 * A connector that empties wishlist.
 */
class ClearWishlist extends DeleteConnector
{
    protected const TABLE_NAME = "wishlist";
    protected const ID_COLUMN = "username";
    protected const ID_COLUMN_TYPE = "s";
}