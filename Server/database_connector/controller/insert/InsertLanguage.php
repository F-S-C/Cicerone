<?php

namespace database_connector\controller\insert;

require_once "/membri/fsc/database_connector/controller/insert/InsertConnector.php";

/**
 * Insert a language.
 * @package database_connector\controller\insert
 */
class InsertLanguage extends InsertConnector
{
    protected const COLUMNS = "language_code, language_name";
    protected const COLUMNS_TYPE = "ss";
    protected const TABLE_NAME = "language";
}