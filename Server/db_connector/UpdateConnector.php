<?php


namespace db_connector;

use Exception;
use InvalidArgumentException;
use mysqli_sql_exception;

require_once("BooleanConnector.php");

abstract class UpdateConnector extends BooleanConnector
{
    protected const TABLE_NAME = "";
    protected const ID_COLUMN = "";
    protected const ID_COLUMN_TYPE = "";
    protected $id;
    protected $values_to_update = array();
    protected $types = array();


    public function __construct($id)
    {
        $this->id = $id;
        parent::__construct();
    }

    public function add_value(string $column, string $value = null, string $type = null)
    {
        if (!isset($value)) {
            return;
        }
        $this->values_to_update[$column] = $value;
        $this->types[$column] = $type;
    }

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