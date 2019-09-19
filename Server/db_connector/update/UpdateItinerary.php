<?php

namespace db_connector\update;

require_once "UpdateConnector.php";

/**
 * Update an itinerary.
 * @package db_connector
 */
class UpdateItinerary extends UpdateConnector
{
    protected const TABLE_NAME = "itinerary";
    protected const ID_COLUMN = "itinerary_code";
    protected const ID_COLUMN_TYPE = "i";
}