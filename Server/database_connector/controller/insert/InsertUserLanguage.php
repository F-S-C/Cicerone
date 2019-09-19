<?php

namespace database_connector\controller\insert;

require_once "InsertConnector.php";

/**
 * Insert a language for a user.
 * @package database_connector\controller\insert
 */
class InsertUserLanguage extends InsertConnector
{
    protected const COLUMNS = "username, language_code";
    protected const COLUMNS_TYPE = "ss";
    protected const TABLE_NAME = "user_language";
}