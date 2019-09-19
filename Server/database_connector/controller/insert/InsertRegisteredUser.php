<?php

namespace database_connector\controller\insert;

require_once "InsertConnector.php";

/**
 * Insert a new user.
 * @package database_connector\controller\insert
 */
class InsertRegisteredUser extends InsertConnector
{
    protected const COLUMNS = "username, tax_code, name, surname, password, email, user_type, cellphone, birth_date, sex";
    protected const COLUMNS_TYPE = "ssssssisss";
    protected const TABLE_NAME = "registered_user";
}