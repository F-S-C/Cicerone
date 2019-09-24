<?php

namespace database_connector\controller\request;

use database_connector\controller\JsonConnector;

require_once "/home/fsc/www/database_connector/controller/JsonConnector.php";
require_once "/home/fsc/www/database_connector/controller/request/RequestRegisteredUser.php";
require_once "/home/fsc/www/database_connector/controller/request/RequestItinerary.php";

/**
 * Request all the itineraries from a wishlist of a user.
 */
class RequestWishlist extends JsonConnector
{
    /** @var string|null The wishlist's owner's username */
    private $owner;
    /** @var string|null The itinerary code to be searched in the wishlist. */
    private $itinerary;

    /**
     * RequestWishlist constructor.
     * @param string|null $owner The wishlist's owner's username.
     * @param string|null $itinerary The itinerary code to be searched in the wishlist.
     */
    public function __construct(string $owner = null, string $itinerary = null)
    {
        $this->owner = isset($owner) && $owner != "" ? strtolower($owner) : null;
        $this->itinerary = isset($itinerary) && $itinerary != "" ? $itinerary : null;
        parent::__construct();
    }

    /**
     * @see JsonConnector::fetch_all_rows()
     */
    protected function fetch_all_rows(): array
    {
        $query = "SELECT * FROM wishlist";
        $conditions = array();
        $data = array();
        $types = "";
        if (isset($this->owner)) {
            array_push($conditions, "username = ?");
            array_push($data, $this->owner);
            $types .= "s";
        }
        if (isset($this->itinerary)) {
            array_push($conditions, "itinerary_in_wishlist = ?");
            array_push($data, $this->itinerary);
            $types .= "i";
        }
        $query .= $this->create_SQL_WHERE_clause($conditions);

        $to_return = $this->execute_query($query, $data, $types);

        foreach ($to_return as &$row) {
            $row["itinerary_in_wishlist"] = $this->get_from_connector(new RequestItinerary(null, null, null, null, $row["itinerary_in_wishlist"]))[0];
            $row["username"] = $this->get_from_connector(new RequestRegisteredUser($row["username"]))[0];
        }

        return $to_return;
    }
}