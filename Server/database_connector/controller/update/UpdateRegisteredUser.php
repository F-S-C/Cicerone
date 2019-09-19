<?php

namespace database_connector\controller\update;

require_once "UpdateConnector.php";

/**
 * Update a user.
 * @package database_connector\controller\update
 */
class UpdateRegisteredUser extends UpdateConnector
{
    protected const TABLE_NAME = "registered_user";
    protected const ID_COLUMN = "username";
    protected const ID_COLUMN_TYPE = "s";
}