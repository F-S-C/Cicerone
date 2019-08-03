<?php

namespace db_connector;

use Exception;

require_once("DatabaseConnector.php");

/**
 * A type of connector that fetches data from a Database and converts the results of the interrogation to the
 * JSON format.
 * @package db_connector
 */
abstract class JsonConnector extends DatabaseConnector
{
    /**
     * Fetch all needed rows from MySQL database.
     * @return array The array of rows.
     * @throws Exception if there were errors in fetching the data.
     */
    protected abstract function fetch_all_rows(): array;

    /**
     * Fetch data from the database and convert it to a string.
     * @return string A string that contains the fetched data.
     */
    public function get_content(): string
    {
        try {
            $rows = $this->fetch_all_rows();
        } catch (Exception $e) {
            $rows = array();
        }

        return json_encode($rows);
    }
}