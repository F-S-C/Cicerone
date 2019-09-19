<?php

namespace database_connector\controller\request;

use database_connector\controller\JsonConnector;

require_once "/membri/fsc/database_connector/controller/JsonConnector.php";
require_once "/membri/fsc/database_connector/controller/request/RequestRegisteredUser.php";
require_once "/membri/fsc/database_connector/controller/request/RequestItinerary.php";

/**
 * Request the itineraries in a reservation alongside with some their information.
 * @package database_connector\controller\request
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
            $row['booked_itinerary'] = $this->get_from_connector(new RequestItinerary(null, null, null, null, $row["booked_itinerary"]))[0];
            $row['username'] = $this->get_from_connector(new RequestRegisteredUser($row["username"]))[0];
        }

        return $to_return;
    }
}