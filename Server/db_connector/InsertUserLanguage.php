<?php

namespace db_connector;

require_once("InsertConnector.php");

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

$connector = new InsertUserLanguage();
$connector->add_value(array(strtolower($_POST['username']), $_POST['language_code']));
print $connector->get_content();