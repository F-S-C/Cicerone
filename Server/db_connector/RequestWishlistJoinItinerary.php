<?php

namespace db_connector;

use InvalidArgumentException;
use mysqli_sql_exception;

require_once("JsonConnector.php");

class RequestWishlistJoinItinerary extends JsonConnector
{
    private $owner;

    public function __construct(string $owner = null)
    {
        $this->owner = isset($owner) && $owner != "" ? strtolower($owner) : null;
        parent::__construct();
    }

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

$connector = new RequestWishlistJoinItinerary($_POST['username']);
print $connector->get_content();