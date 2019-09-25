<?php

namespace database_connector\controller\update;

require_once "/home/fsc/www/database_connector/controller/update/UpdateConnector.php";

/**
 * Update a document.
 */
class UpdateDocument extends UpdateConnector
{
    protected const TABLE_NAME = "document";
    protected const ID_COLUMN = "username";
    protected const ID_COLUMN_TYPE = "s";
}