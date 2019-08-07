<?php

namespace db_connector;

require_once("DeleteConnector.php");

/**
 * A connector that deletes an itinerary.
 * @package db_connector
 */
class DeleteItinerary extends DeleteConnector
{
    protected const TABLE_NAME = "itinerary";
    protected const ID_COLUMN = "itinerary_code";
    protected const ID_COLUMN_TYPE = "i";
}

$connector = new DeleteItinerary($_POST['itinerary_code']);
print $connector->get_content();