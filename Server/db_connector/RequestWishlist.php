<?php

namespace db_connector;

use mysqli_sql_exception;

require_once("JsonConnector.php");

/**
 * Request all the itineraries from a wishlist of a user.
 * @package db_connector
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
            $parameters = array($row["itinerary_in_wishlist"]);
            $row["itinerary_in_wishlist"] = $this->execute_query("SELECT * FROM itinerary WHERE itinerary_code = ?", $parameters, "i")[0];
            $parameters = array($row["username"]);
            $row["username"] = $this->execute_query("SELECT * FROM registered_user WHERE username = ?", $parameters, "s")[0];
        }

        return $to_return;
    }
}

$connector = new RequestWishlist($_POST['username'], $_POST['itinerary_in_wishlist']);
print $connector->get_content();