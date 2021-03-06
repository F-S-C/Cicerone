<?php

namespace database_connector\controller\update;

require_once "/home/fsc/www/database_connector/controller/update/UpdateConnector.php";

/**
 * Update a user.
 */
class UpdateRegisteredUser extends UpdateConnector
{
    protected const TABLE_NAME = "registered_user";
    protected const ID_COLUMN = "username";
    protected const ID_COLUMN_TYPE = "s";
}