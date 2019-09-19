<?php

namespace db_connector\insert;

require_once "InsertConnector.php";

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