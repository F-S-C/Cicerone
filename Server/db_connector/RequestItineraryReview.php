<?php


namespace db_connector;

require_once "JsonConnector.php";
require_once "RequestRegisteredUser.php";
require_once "RequestItinerary.php";

/**
 * Request all reviews for an itinerary.
 * @package db_connector
 */
class RequestItineraryReview extends JsonConnector
{
    /** @var string|null The username of the author of the reviews. */
    private $username;
    /** @var string|null The reviewed itinerary code. */
    private $reviewed_itinerary;

    /**
     * RequestItineraryReview constructor.
     * @param string|null $username The username of the author of the reviews.
     * @param string|null $reviewed_itinerary The reviewed itinerary code.
     */
    public function __construct(string $username = null, string $reviewed_itinerary = null)
    {
        $this->username = isset($username) && $username != "" ? strtolower($username) : null;
        $this->reviewed_itinerary = isset($reviewed_itinerary) && $reviewed_itinerary != "" ? $reviewed_itinerary : null;
        parent::__construct();
    }

    /**
     * @see JsonConnector::fetch_all_rows()
     */
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

        $to_return = $this->execute_query($query, $data, $types);
        foreach ($to_return as &$row) {
            $row["username"] = $this->get_from_connector(new RequestRegisteredUser($row["username"]))[0];
            $row["reviewed_itinerary"] = $this->get_from_connector(new RequestItinerary(null, null, null, null, $row["reviewed_itinerary"]))[0];
        }

        return $to_return;
    }
}