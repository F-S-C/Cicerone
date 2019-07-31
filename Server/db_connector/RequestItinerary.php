<?php

namespace db_connector;

use mysqli_sql_exception;

require_once("JsonConnector.php");

class RequestItinerary extends JsonConnector
{
    private $owner;
    private $location;
    private $beginning_date;
    private $ending_date;
    private $code;

    /**
     * RequestItinerary constructor.
     * @param $owner
     * @param $location
     * @param $beginning_date
     * @param $ending_date
     * @param $code
     */
    public function __construct(string $owner = null, string $location = null, string $beginning_date = null, string $ending_date = null, string $code = null)
    {
        $this->owner = $owner;
        $this->location = $location;
        $this->beginning_date = $beginning_date;
        $this->ending_date = $ending_date;
        $this->code = $code;
        parent::__construct();
    }


    protected function fetch_all_rows(): array
    {
        $query = "SELECT * FROM itinerary";

        $conditions = array();
        $data = array();
        $types = "";
        if (isset($this->owner)) {
            array_push($conditions, "username = ?");
            array_push($data, $this->owner);
            $types .= "s";
        }
        if (isset($this->location)) {
            array_push($conditions, "location = ?");
            array_push($data, $this->location);
            $types .= "s";
        }
        if (isset($this->code)) {
            array_push($conditions, "itinerary_code = ?");
            array_push($data, $this->code);
            $types .= "i";
        }
        if (isset($this->beginning_date) || isset($this->ending_date)) {
            $date_condition = $this->get_date_condition();
            array_push($conditions, $date_condition);
            if (substr_count($date_condition, "?") == 6) {
                array_push($data, $this->beginning_date, $this->ending_date, $this->beginning_date, $this->ending_date, $this->beginning_date, $this->ending_date);
            } else if (isset($this->beginning_date)) {
                array_push($data, $this->beginning_date);
            } else {
                array_push($data, $this->ending_date);
            }
            $types .= str_repeat("s", substr_count($date_condition, "?"));
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

    private function get_date_condition(): string
    {
        $date_condition = "";
        if (isset($this->beginning_date) && isset($this->ending_date)) {
            $date_condition = "(beginning_date <= ? AND ending_date >= ?) OR (beginning_date >= ? AND ending_date <= ?) OR (beginning_date <= ? AND ending_date >= ?)";
        } else if (isset($this->beginning_date)) {
            $date_condition = "beginning_date <= ?";
        } else if (isset($this->ending_date)) {
            $date_condition = "ending_date >= ?";
        }
        return $date_condition;
    }
}

$connector = new RequestItinerary($_POST['username'], $_POST['location'], $_POST['beginning_date'], $_POST['ending_date'], $_POST['itinerary_code']);
print $connector->get_content();