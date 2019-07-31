<?php


namespace db_connector;

use mysqli_sql_exception;

require_once("JsonConnector.php");

class RequestReservation extends JsonConnector
{
    private $username;
    private $itinerary;

    public function __construct(string $username = null, string $itinerary = null)
    {
        $this->username = $username;
        $this->itinerary = $itinerary;
        parent::__construct();
    }

    protected function fetch_all_rows(): array
    {
        $query = "SELECT * FROM reservation";
        $conditions = array();
        $data = array();
        $types = "";
        if (isset($this->username)) {
            array_push($conditions, "username = ?");
            array_push($data, $this->username);
            $types .= "s";
        }
        if (isset($this->itinerary)) {
            array_push($conditions, "booked_itinerary = ?");
            array_push($data, $this->itinerary);
            $types .= "i";
        }
        $query .= $this->create_SQL_WHERE_clause($conditions);

        if($statement = $this->connection->prepare($query)){
            if (isset($this->username) || isset($this->itinerary)){
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

$connector = new RequestReservation($_POST['username'], $_POST['booked_itinerary']);
print $connector->get_content();