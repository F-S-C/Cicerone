<?php

namespace db_connector\insert;

require_once "InsertConnector.php";

/**
 * Insert a language for a user.
 * @package db_connector
 */
class InsertUserLanguage extends InsertConnector
{
    protected const COLUMNS = "username, language_code";
    protected const COLUMNS_TYPE = "ss";
    protected const TABLE_NAME = "user_language";
}