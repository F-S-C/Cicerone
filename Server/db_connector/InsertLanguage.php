<?php

namespace db_connector;

require_once "InsertConnector.php";

/**
 * Insert a language.
 * @package db_connector
 */
class InsertLanguage extends InsertConnector
{
    protected const COLUMNS = "language_code, language_name";
    protected const COLUMNS_TYPE = "ss";
    protected const TABLE_NAME = "language";
}

$connector = new InsertLanguage();
$connector->add_value(array($_POST['language_code'], $_POST['language_name']));
print $connector->get_content();