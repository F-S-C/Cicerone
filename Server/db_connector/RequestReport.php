<?php


namespace db_connector;

use mysqli_sql_exception;

require_once("JsonConnector.php");

class RequestReport extends JsonConnector
{
    private $report_code;
    private $username;
    private $reported_user;

    public function __construct(string $report_code = null, string $reported_user = null, string $username = null)
    {
        $this->report_code = $report_code;
        $this->username = $username;
        $this->reported_user = $reported_user;
        parent::__construct();
    }

    protected function fetch_all_rows(): array
    {
        $query = "SELECT * FROM report";

        $conditions = array();
        $data = array();
        $types = "";
        if (isset($this->reported_user)) {
            array_push($conditions, "reported_user = ?");
            array_push($data, $this->reported_user);
            $types .= "s";
        }
        if (isset($this->username)) {
            array_push($conditions, "username = ?");
            array_push($data, $this->username);
            $types .= "s";
        }
        if (isset($this->report_code)) {
            array_push($conditions, "report_code = ?");
            array_push($data, $this->report_code);
            $types .= "s";
        }
        $query .= $this->create_SQL_WHERE_clause($conditions);


        if ($statement = $this->connection->prepare($query)) {
            if (isset($this->reported_user) || isset($this->username)) {
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

$connector = new RequestReport($_POST['report_code'], $_POST['reported_user'], $_POST['username']);
print $connector->get_content();