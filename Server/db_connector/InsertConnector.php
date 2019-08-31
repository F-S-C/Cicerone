<?php


namespace db_connector;

use Exception;
use InvalidArgumentException;
use mysqli_sql_exception;

require_once("BooleanConnector.php");

/**
 * A generic connector used to add data to a table in a database.
 * @package db_connector
 */
abstract class InsertConnector extends BooleanConnector
{
    /** @var string The columns of the table. */
    protected const COLUMNS = "";
    /** @var string The type of the columns (in a mysqli prepared statement format). */
    protected const COLUMNS_TYPE = "";
    /** @var string The name of the table. */
    protected const TABLE_NAME = "";
    /** @var array The values to be added. */
    protected $values_to_add = array();

    /**
     * Add a value to be inserted
     * @param array $value The value to be inserted. The array must be an associative array where the keys are the name
     * of the columns and the values are the values that the new element will have for a specific column. This function
     * does not check for the correctness of the array format.
     */
    function add_value(array $value)
    {
        array_push($this->values_to_add, $value);
    }

    /**
     * Insert the data into the table and get the result of the operation.
     * @return string The result of the operation.
     * @see DatabaseConnector::get_content()
     */
    public function get_content(): string
    {
        $query = "INSERT INTO " . $this::TABLE_NAME . "(" . $this::COLUMNS . ") VALUES ";
        foreach ($this->values_to_add as $value) {
            $prepared = substr(str_repeat("?,", substr_count($this::COLUMNS, ",") + 1), 0, -1);
            $query .= "(" . $prepared . "), ";
        }
        $query = substr($query, 0, -2);

        $data = array();
        array_walk_recursive($this->values_to_add, function ($a) use (&$data) {
            $data[] = $a;
        });

        $types = str_repeat($this::COLUMNS_TYPE, sizeof($this->values_to_add));

        try {
            if ($statement = $this->connection->prepare($query)) {
                $statement->bind_param($types, ...$data);
                if ($statement->execute()) {
                    $to_return = self::get_true();
                } else {
                    throw new mysqli_sql_exception($statement->error);
                }
            } else {
                throw new mysqli_sql_exception($this->connection->error);
            }
        } catch (Exception $e) {
            $to_return = self::get_false($e->getMessage());
        }

        return json_encode($to_return);
    }


}