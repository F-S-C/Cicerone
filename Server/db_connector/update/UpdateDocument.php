<?php

namespace db_connector\update;

require_once "UpdateConnector.php";

/**
 * Update a document.
 * @package db_connector
 */
class UpdateDocument extends UpdateConnector
{
    protected const TABLE_NAME = "document";
    protected const ID_COLUMN = "username";
    protected const ID_COLUMN_TYPE = "s";
}