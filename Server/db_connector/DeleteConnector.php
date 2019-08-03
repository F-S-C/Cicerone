<?php

namespace db_connector;

require_once("BooleanConnector.php");

abstract class DeleteConnector extends BooleanConnector
{
    protected const TABLE_NAME = "table";
    protected const ID_COLUMN = "id_col";
    protected const ID_COLUMN_TYPE = "s";
    protected $id;

    public function __construct(string $id = null)
    {
        if(!isset($id) || $id == ""){
            die(json_encode(self::get_false("Some required fields are missing.")));
        }
        $this->id = $id;
        parent::__construct();
    }

    public function get_content(): string
    {
        $query = "DELETE FROM " . $this::TABLE_NAME . " WHERE " . $this::ID_COLUMN . " = ?";
        if($statement = $this->connection->prepare($query))
        {
            $statement->bind_param($this::ID_COLUMN_TYPE, $this->id);
            if($statement->execute()){
                $to_return = self::get_true();
            }
            else{
                $to_return = self::get_false($statement->error);
            }
        }
        else{
            $to_return = self::get_false($this->connection->error);
        }

        return json_encode($to_return);
    }
}