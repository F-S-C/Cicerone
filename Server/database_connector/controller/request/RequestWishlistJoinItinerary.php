<?php

namespace database_connector\controller\request;

use database_connector\controller\JsonConnector;
use mysqli_sql_exception;

require_once "/home/fsc/www/database_connector/controller/JsonConnector.php";

/**
 * Request the itineraries in a wishlist alongside with all their information.
 */
class RequestWishlistJoinItinerary extends JsonConnector
{
    /** @var string|null The wishlist's owner's username. */
    private $owner;

    /**
     * RequestWishlistJoinItinerary constructor.
     * @param string|null $owner The wishlist's owner's username.
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
        $query = "SELECT * FROM wishlist, itinerary WHERE wishlist.itinerary_in_wishlist = itinerary.itinerary_code";
        if ($this->owner) {
            $query .= " AND wishlist.username = ?";
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