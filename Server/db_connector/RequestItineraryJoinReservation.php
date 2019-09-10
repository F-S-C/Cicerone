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
    private $itinerary_owner;

    /** @var string|null The itinerary's author's username. */
    private $globetrotter;

    /**
     * RequestItineraryJoinReservation constructor.
     * @param string|null $globetrotter The reservation's globetrotter.
     * @param string|null $owner The itinerary's author's username.
     */
    public function __construct(string $globetrotter = null, string $owner = null)
    {
        $this->itinerary_owner = isset($owner) && $owner != "" ? strtolower($owner) : null;
        $this->globetrotter = isset($globetrotter) && $globetrotter != "" ? strtolower($globetrotter) : null;
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
        if ($this->itinerary_owner) {
            $query .= " AND itinerary.username = ?";
            array_push($parameters, $this->itinerary_owner);
            $types .= "s";
        }
        if ($this->globetrotter) {
            $query .= " AND reservation.username = ?";
            array_push($parameters, $this->globetrotter);
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

$connector = new RequestItineraryJoinReservation($_POST['username'], $_POST['cicerone']);
print $connector->get_content();