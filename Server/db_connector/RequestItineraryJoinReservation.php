<?php

namespace db_connector;

require_once("JsonConnector.php");

/**
 * Request the itineraries in a reservation alongside with some their information.
 * @package db_connector
 */
class RequestItineraryJoinReservation extends JsonConnector
{
    /** @var string|null The itinerary's author's username. */
    private $owner;

    /**
     * RequestItineraryJoinReservation constructor.
     * @param string|null $owner The itinerary's author's username.
     */
    public function __construct(string $owner = null)
    {
        $this->owner = isset($owner) && $owner != "" ? strtolower($owner) : null;
        parent::__construct();
    }

    /**
     * @see JsonConnector::fetch_all_rows()
     */
    protected function fetch_all_rows(): array
    {
        $query = "SELECT reservation.* FROM reservation, itinerary WHERE reservation.booked_itinerary = itinerary.itinerary_code";
        $parameters = array();
        $types = "";
        if ($this->owner) {
            $query .= " AND itinerary.username = ?";
            array_push($parameters, $this->owner);
            $types .= "s";
        }
        $to_return = $this->execute_query($query, $parameters, $types);

        foreach ($to_return as &$row) {
            $query = "SELECT * FROM itinerary WHERE itinerary_code = ?";
            $parameters = array($row['booked_itinerary']);
            $row['booked_itinerary'] = $this->execute_query($query, $parameters, "i")[0];
        }

        foreach ($to_return as &$row) {
            $query = "SELECT * FROM registered_user WHERE username = ?";
            $parameters = array($row['username']);
            $row['username'] = $this->execute_query($query, $parameters, "s")[0];
        }

        return $to_return;
    }
}

$connector = new RequestItineraryJoinReservation($_POST['username']);
print $connector->get_content();