<?php

namespace database_connector\controller\request;

use database_connector\controller\JsonConnector;

require_once "/home/fsc/www/database_connector/controller/JsonConnector.php";
require_once "/home/fsc/www/database_connector/controller/request/RequestRegisteredUser.php";

/**
 * Request all the itineraries that match a set of criteria.
 */
class RequestActiveItinerary extends JsonConnector
{
    /** @var string|null The owner of the itinerary. */
    private $owner;
    /** @var string|null The location of the itinerary. */
    private $location;
    /** @var string|null The beginning date of the itinerary. */
    private $beginning_date;
    /** @var string|null The ending date of the itinerary. */
    private $ending_date;
    /** @var string|null The identification code of the itinerary. */
    private $code;

    /**
     * RequestItinerary constructor.
     * @param string|null $owner The owner of the itinerary.
     * @param string|null $location The location of the itinerary.
     * @param string|null $beginning_date The beginning date of the itinerary.
     * @param string|null $ending_date The ending date of the itinerary.
     * @param string|null $code The identification code of the itinerary.
     */
    public function __construct(string $owner = null, string $location = null, string $beginning_date = null, string $ending_date = null, string $code = null)
    {
        $this->owner = isset($owner) && $owner != "" ? strtolower($owner) : null;
        $this->location = isset($location) && $location == "" ? $location : null;
        $this->beginning_date = isset($beginning_date) && $beginning_date == "" ? $beginning_date : null;
        $this->ending_date = isset($ending_date) && $ending_date == "" ? $ending_date : null;
        $this->code = isset($code) && $code == "" ? $code : null;
        parent::__construct();
    }

    /**
     * @see JsonConnector::fetch_all_rows()
     */
    protected function fetch_all_rows(): array
    {
        $query = "SELECT * FROM itinerary";

        $conditions = array();
        $data = array();
        $types = "";
        array_push($conditions, "ending_date >= CURRENT_DATE");

        if (isset($this->owner)) {
            array_push($conditions, "username = ?");
            array_push($data, $this->owner);
            $types .= "s";
        } else {
            array_push($conditions, "username NOT IN ('deleted_user', 'admin')");
            $position = sizeof($conditions) - 1;
        }
        if (isset($this->location)) {
            array_push($conditions, "location = ?");
            array_push($data, $this->location);
            $types .= "s";
        }
        if (isset($this->code)) {
            array_push($conditions, "itinerary_code = ?");
            array_push($data, $this->code);
            if (isset($position)) {
                array_splice($conditions, $position, 1);
            }
            $types .= "i";
        }
        if (isset($this->beginning_date) || isset($this->ending_date)) {
            $date_condition = $this->get_date_condition();
            array_push($conditions, $date_condition);
            if (substr_count($date_condition, "?") == 6) {
                array_push($data, $this->beginning_date, $this->ending_date, $this->beginning_date, $this->ending_date, $this->beginning_date, $this->ending_date);
            } else if (isset($this->beginning_date)) {
                array_push($data, $this->beginning_date);
            } else {
                array_push($data, $this->ending_date);
            }
            $types .= str_repeat("s", substr_count($date_condition, "?"));
        }

        $query .= $this->create_SQL_WHERE_clause($conditions);

        $to_return = $this->execute_query($query, $data, $types);
        foreach ($to_return as &$row) {
            $row["username"] = $this->get_from_connector(new RequestRegisteredUser($row["username"]))[0];
        }

        return $to_return;
    }

    /**
     * Get the SQL condition to match the given dates.
     * @return string The SQL condition to be put in the WHERE clause.
     */
    private function get_date_condition(): string
    {
        $date_condition = "";
        if (isset($this->beginning_date) && isset($this->ending_date)) {
            $date_condition = "(beginning_date <= ? AND ending_date >= ?) OR (beginning_date >= ? AND ending_date <= ?) OR (beginning_date <= ? AND ending_date >= ?)";
        } else if (isset($this->beginning_date)) {
            $date_condition = "beginning_date <= ?";
        } else if (isset($this->ending_date)) {
            $date_condition = "ending_date >= ?";
        }
        return $date_condition;
    }
}