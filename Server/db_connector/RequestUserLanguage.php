<?php


namespace db_connector;

use mysqli_sql_exception;

require_once("JsonConnector.php");

class RequestUserLanguage extends JsonConnector
{
    private $language;
    private $username;

    public function __construct(string $language = null, string $username = null)
    {
        $this->language = $language;
        $this->username = $username;
        parent::__construct();
    }

    protected function fetch_all_rows(): array
    {
        $query = "SELECT * FROM user_language";

        $conditions = array();
        $data = array();
        $types = "";
        if (isset($this->language)) {
            array_push($conditions, "language_code = ?");
            array_push($data, $this->language);
            $types .= "s";
        }
        if (isset($this->username)) {
            array_push($conditions, "username = ?");
            array_push($data, $this->username);
            $types .= "s";
        }
        $query .= $this->create_SQL_WHERE_clause($conditions);


        if ($statement = $this->connection->prepare($query)) {
            if (isset($this->language) || isset($this->username)) {
                $statement->bind_param($types, ...$data);
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

$connector = new RequestItineraryLanguage($_POST['language_code'], $_POST['username']);
print $connector->get_content();