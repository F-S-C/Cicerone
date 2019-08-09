<?php

namespace db_connector;

require_once("BooleanConnector.php");

/**
 * A generic connector that deletes data from a database.
 * @package db_connector
 */
abstract class DeleteConnector extends BooleanConnector
{
    /** @var string The name of the table that the connector will modify. */
    protected const TABLE_NAME = "table";
    /** @var string The name of the column that identifies the tuples in the table. */
    protected const ID_COLUMN = "id_col";
    /**
     * @var string The type of the column that identifies the tuples in the table.
     * @note It must be a type accepted by the mysqli prepared stamements' bind_param ("s", "i", "d" or "b").
     */
    protected const ID_COLUMN_TYPE = "s";
    /** @var string The ID of the object to be deleted. */
    protected $id;

    /**
     * DeleteConnector constructor.
     * @param string $id The ID of the object to be deleted.
     * @warning If the ID is not set, the connector will die with an error.
     */
    public function __construct(string $id = null)
    {
        if (!isset($id) || $id == "") {
            die(json_encode(self::get_false("Some required fields are missing.")));
        }
        $this->id = $id;
        parent::__construct();
    }

    /**
     * Delete the object from the database and get the result of the operation.
     * @return string The result of the operation.
     * @see DatabaseConnector::get_content()
     */
    public function get_content(): string
    {
        $query = "DELETE FROM " . $this::TABLE_NAME . " WHERE " . $this::ID_COLUMN . " = ?";
        if ($statement = $this->connection->prepare($query)) {
            $statement->bind_param($this::ID_COLUMN_TYPE, $this->id);
            if ($statement->execute()) {
                $to_return = self::get_true();
            } else {
                $to_return = self::get_false($statement->error);
            }
        } else {
            $to_return = self::get_false($this->connection->error);
        }

        return json_encode($to_return);
    }
}
