<?php


namespace db_connector;

use mysqli_sql_exception;

require_once("JsonConnector.php");

class RequestItineraryLanguage extends JsonConnector
{
    private $language;
    private $itinerary;

    public function __construct(string $language = null, string $itinerary = null)
    {
        $this->language = $language;
        $this->itinerary = $itinerary;
        parent::__construct();
    }

    protected function fetch_all_rows(): array
    {
        $query = "SELECT * FROM itinerary_language";

        $conditions = array();
        $data = array();
        $types = "";
        if (isset($this->language)) {
            array_push($conditions, "language_code = ?");
            array_push($data, $this->language);
            $types .= "s";
        }
        if (isset($this->itinerary)) {
            array_push($conditions, "itinerary_code = ?");
            array_push($data, $this->itinerary);
            $types .= "i";
        }
        $query .= $this->create_SQL_WHERE_clause($conditions);


        if ($statement = $this->connection->prepare($query)) {
            if (isset($this->language) || isset($this->itinerary)) {
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

$connector = new RequestItineraryLanguage($_POST['language_code'], $_POST['itinerary_code']);
print $connector->get_content();