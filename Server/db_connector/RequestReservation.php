<?php


namespace db_connector;

use mysqli_sql_exception;

require_once("JsonConnector.php");

/**
 * Request a reservation for an itinerary.
 * @package db_connector
 */
class RequestReservation extends JsonConnector
{
    /** @var string|null The reservation author's username. */
    private $username;
    /** @var string|null The reserved itinerary's code. */
    private $itinerary;

    /**
     * RequestReservation constructor.
     * @param string|null $username The reservation author's username.
     * @param string|null $itinerary The reserved itinerary's code.
     */
    public function __construct(string $username = null, string $itinerary = null)
    {
        $this->username = isset($username) && $username != "" ? strtolower($username) : null;
        $this->itinerary = isset($itinerary) && $itinerary != "" ? $itinerary : null;
        parent::__construct();
    }

    /**
     * @see JsonConnector::fetch_all_rows()
     */
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

        $to_return = $this->execute_query($query, $data, $types);

        return $to_return;
    }


}

$connector = new RequestReservation($_POST['username'], $_POST['booked_itinerary']);
print $connector->get_content();