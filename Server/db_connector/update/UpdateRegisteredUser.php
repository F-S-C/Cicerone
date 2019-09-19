<?php

namespace db_connector\update;

require_once "UpdateConnector.php";

/**
 * Update a user.
 * @package db_connector
 */
class UpdateRegisteredUser extends UpdateConnector
{
    protected const TABLE_NAME = "registered_user";
    protected const ID_COLUMN = "username";
    protected const ID_COLUMN_TYPE = "s";
}