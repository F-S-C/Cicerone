<?php

namespace database_connector\controller\update;

require_once "/membri/fsc/database_connector/controller/update/UpdateConnector.php";

/**
 * Update an itinerary.
 */
class UpdateItinerary extends UpdateConnector
{
    protected const TABLE_NAME = "itinerary";
    protected const ID_COLUMN = "itinerary_code";
    protected const ID_COLUMN_TYPE = "i";
}