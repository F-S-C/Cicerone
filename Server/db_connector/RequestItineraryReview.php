<?php


namespace db_connector;

use mysqli_sql_exception;

require_once("JsonConnector.php");

class RequestItineraryReview extends JsonConnector
{
    private $username;
    private $reviewed_itinerary;

    public function __construct(string $username = null, string $reviewed_itinerary = null)
    {
        $this->username = isset($username) && $username != "" ? strtolower($username) : null;
        $this->reviewed_itinerary = isset($reviewed_itinerary) && $reviewed_itinerary != "" ? $reviewed_itinerary : null;
        parent::__construct();
    }

    protected function fetch_all_rows(): array
    {
        $query = "SELECT * FROM itinerary_review";

        $conditions = array();
        $data = array();
        $types = "";
        if (isset($this->username)) {
            array_push($conditions, "username = ?");
            array_push($data, $this->username);
            $types .= "s";
        }
        if (isset($this->reviewed_itinerary)) {
            array_push($conditions, "reviewed_itinerary = ?");
            array_push($data, $this->reviewed_itinerary);
            $types .= "i";
        }
        $query .= $this->create_SQL_WHERE_clause($conditions);


        if ($statement = $this->connection->prepare($query)) {
            if (isset($this->username) || isset($this->reviewed_itinerary)) {
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

$connector = new RequestItineraryReview($_POST['username'], $_POST['reviewed_itinerary']);
print $connector->get_content();