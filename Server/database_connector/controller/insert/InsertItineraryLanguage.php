<?php

namespace database_connector\controller\insert;

require_once "InsertConnector.php";

/**
 * Insert a language for an itinerary.
 * @package database_connector\controller\insert
 */
class InsertItineraryLanguage extends InsertConnector
{
    protected const COLUMNS = "itinerary_code, language_code";
    protected const COLUMNS_TYPE = "is";
    protected const TABLE_NAME = "itinerary_language";
}