<?php

namespace database_connector\controller\delete;

use database_connector\controller\DeleteConnector;

require_once "../DeleteConnector.php";

/**
 * A connector that deletes an itinerary.
 * @package database_connector\controller\delete
 */
class DeleteItinerary extends DeleteConnector
{
    protected const TABLE_NAME = "itinerary";
    protected const ID_COLUMN = "itinerary_code";
    protected const ID_COLUMN_TYPE = "i";
}