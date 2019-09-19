<?php

namespace db_connector\insert;

require_once "InsertConnector.php";

/**
 * Insert a new user.
 * @package db_connector
 */
class InsertRegisteredUser extends InsertConnector
{
    protected const COLUMNS = "username, tax_code, name, surname, password, email, user_type, cellphone, birth_date, sex";
    protected const COLUMNS_TYPE = "ssssssisss";
    protected const TABLE_NAME = "registered_user";
}