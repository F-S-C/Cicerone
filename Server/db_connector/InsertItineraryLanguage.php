<?php

namespace db_connector;

require_once("InsertConnector.php");

/**
 * Insert a language for an itinerary.
 * @package db_connector
 */
class InsertItineraryLanguage extends InsertConnector
{
    protected const COLUMNS = "itinerary_code, language_code";
    protected const COLUMNS_TYPE = "is";
    protected const TABLE_NAME = "itinerary_language";
}

$connector = new InsertItineraryLanguage();
$connector->add_value(array($_POST['itinerary_code'], $_POST['language_code']));
print $connector->get_content();