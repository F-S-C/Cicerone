<?php

namespace database_connector\controller\insert;

require_once "/membri/fsc/database_connector/controller/insert/InsertConnector.php";

/**
 * Insert a language for a user.
 */
class InsertUserLanguage extends InsertConnector
{
    protected const COLUMNS = "username, language_code";
    protected const COLUMNS_TYPE = "ss";
    protected const TABLE_NAME = "user_language";
}