<?php

namespace database_connector\controller;

use Exception;
use mysqli_sql_exception;

require_once "/home/fsc/www/database_connector/controller/DatabaseConnector.php";

/**
 * A type of connector that fetches data from a Database and converts the results of the interrogation to the
 * JSON format.
 */
abstract class JsonConnector extends DatabaseConnector
{
    /**
     * Fetch all needed rows from MySQL database.
     * @return array The array of rows.
     * @throws mysqli_sql_exception if there were errors in fetching the data.
     */
    protected abstract function fetch_all_rows(): array;

    /**
     * Fetch data from the database and convert it to a string.
     * @return string A string that contains the fetched data.
     * @see DatabaseConnector::get_content()
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