<?php

namespace db_connector;

use mysqli_sql_exception;

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
        $query = "SELECT itinerary.title, reservation.username, reservation.number_of_children, reservation.number_of_adults, reservation.requested_date FROM reservation, itinerary WHERE reservation.booked_itinerary = itinerary.itinerary_code";
        if ($this->owner) {
            $query .= " AND itinerary.username = ?";
        }

        if ($statement = $this->connection->prepare($query)) {
            if (isset($this->owner)) {
                $statement->bind_param("s", $this->owner);
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

$connector = new RequestItineraryJoinReservation($_POST['username']);
print $connector->get_content();