<?php

namespace db_connector;

use mysqli_sql_exception;

require_once("JsonConnector.php");

class RequestDocument extends JsonConnector
{
    private $owner;

    public function __construct(string $owner = null)
    {
        $this->owner = strtolower($owner);
        parent::__construct();
    }

    protected function fetch_all_rows(): array
    {
        $query = "SELECT * FROM document";
        if (isset($this->owner)) {
            $query .= " WHERE username = ?";
        }

        if ($statement = $this->connection->prepare($query)) {
            if (isset($this->owner)) {
                $statement->bind_param("s", $this->owner);
            }
            if ($statement->execute()) {
                $to_return = $statement->get_result()->fetch_all(MYSQLI_ASSOC);
            } else {
                throw new mysqli_sql_exception($statement->error);
            }
        } else {
            throw new mysqli_sql_exception($this->connection->error);
        }

        return $to_return;
    }
}

$connector = new RequestDocument($_POST['username']);
print $connector->get_content();