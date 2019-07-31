<?php

namespace db_connector;

use mysqli_sql_exception;

require_once("JsonConnector.php");

class RequestRegisteredUser extends JsonConnector
{
    private $username;
    private $email;

    public function __construct(string $username = null, string $email = null)
    {
        $this->username = strtolower($username);
        $this->email = $email;
        parent::__construct();
    }

    protected function fetch_all_rows(): array
    {
        $query = "SELECT * FROM registered_user";
        $conditions = array();
        $data = array();
        $types = "";
        if (isset($this->username)) {
            array_push($conditions, "username = ?");
            array_push($data, $this->username);
            $types .= "s";
        }
        if (isset($this->email)) {
            array_push($conditions, "email = ?");
            array_push($data, $this->email);
            $types .= "s";
        }
        $query .= $this->create_SQL_WHERE_clause($conditions);

        if($statement = $this->connection->prepare($query)){
            if (isset($this->username) || isset($this->email)){
                $statement->bind_param($types, ...$data);
            }
            if($statement->execute()){
                $to_return = $statement->get_result()->fetch_all(MYSQLI_ASSOC);
            }
            else {
                throw new mysqli_sql_exception($statement->error);
            }
        }
        else{
            throw new mysqli_sql_exception($this->connection->error);
        }
        return $to_return;
    }
}

$connector = new RequestRegisteredUser($_POST['username'], $_POST['email']);
print $connector->get_content();