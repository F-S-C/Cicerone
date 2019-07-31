<?php


namespace db_connector;

use Exception;
use InvalidArgumentException;
use mysqli_sql_exception;

require_once("BooleanConnector.php");

abstract class InsertConnector extends BooleanConnector
{
    protected const COLUMNS = "";
    protected const COLUMNS_TYPE = "";
    protected const TABLE_NAME = "";
    protected $values_to_add = array();

    function add_value(array $value)
    {
        array_push($this->values_to_add, $value);
    }

    public function get_content(): string
    {
        $query = "INSERT INTO " . $this::TABLE_NAME . "(" . $this::COLUMNS . ") VALUES ";
        foreach ($this->values_to_add as $value) {
            $prepared = substr(str_repeat("?,", substr_count($this::COLUMNS, ",") + 1), 0, -1);
            $query .= "(" . $prepared . "), ";
        }
        $query = substr($query, 0, -2);

        $data = array();
        array_walk_recursive($array, function ($a) use (&$data) {
            $data[] = $a;
        });

        $types = str_repeat($this::COLUMNS_TYPE, sizeof($this->values_to_add));

        try {
            foreach ($data as $value) {
                if (!isset($value)) {
                    throw new InvalidArgumentException("Some required fields are missing!");
                }
            }
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