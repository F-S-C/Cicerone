<?php


namespace database_connector\controller\update;

use database_connector\controller\BooleanConnector;
use Exception;
use InvalidArgumentException;
use mysqli_sql_exception;

require_once "/home/fsc/www/database_connector/controller/BooleanConnector.php";

/**
 * A generic connector to update a tuple in a table.
 */
abstract class UpdateConnector extends BooleanConnector
{
    /** @var string The table's name. */
    protected const TABLE_NAME = "";
    /** @var string The table's identifying column's name */
    protected const ID_COLUMN = "";
    /** @var string The table's identifying column's type ("s", "i", "d" or "b", as in mysqli prepared statements). */
    protected const ID_COLUMN_TYPE = "";
    /** @var string The ID of the object to be updated. */
    protected $id;
    /**
     * @var array The new values.
     *
     * It is an associative array that maps the column name with the
     * new value that the specific column must get.
     */
    protected $values_to_update = array();
    /**
     * @var array The new values types.
     *
     * It is an associative array that maps the column name with its type ("s", "i", "d" or "b").
     */
    protected $types = array();

    /**
     * UpdateConnector constructor.
     * @param string $id The ID of the object to be updated.
     */
    public function __construct(string $id)
    {
        $this->id = $id;
        parent::__construct();
    }

    /**
     * Add a column to be updated.
     * @param string $column The name of the column that needs an update.
     * @param string|null $value The new value for the column.
     * @param string|null $type The type of the column ("s", "i", "b" or "d").
     */
    public function add_value(string $column, string $value = null, string $type = null)
    {
        if (!isset($value)) {
            return;
        }
        $this->values_to_update[$column] = $value;
        $this->types[$column] = $type;
    }

    /**
     * Update the table and get the results of the connection.
     * @return string The connection's result.
     */
    public function get_content(): string
    {
        $query = "UPDATE " . $this::TABLE_NAME . " SET ";
        foreach ($this->values_to_update as $key => $value) {
            $query .= $key . " = ?, ";
        }
        $query = substr($query, 0, -2);
        $query .= " WHERE " . $this::ID_COLUMN . " = ?";

        try {
            // Put both types and data in the same order, in order to use them in the prepared statement.
            $ordered_types = "";
            $data = array();
            foreach ($this->values_to_update as $key => $value) {
                if (!isset($value)) {
                    throw new InvalidArgumentException("Some required fields are missing!");
                }
                $ordered_types .= $this->types[$key];
                array_push($data, $value);
            }
            $ordered_types .= $this::ID_COLUMN_TYPE;
            array_push($data, $this->id);

            if (!isset($this->id)) {
                throw new InvalidArgumentException("Some required fields are missing!");
            }

            if ($statement = $this->connection->prepare($query)) {
                $statement->bind_param($ordered_types, ...$data);
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