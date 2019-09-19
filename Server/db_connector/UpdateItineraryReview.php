<?php

namespace db_connector;

use Exception;
use InvalidArgumentException;
use mysqli_sql_exception;

require_once "UpdateConnector.php";

/**
 * Update a itinerary review.
 * @package db_connector
 */
class UpdateItineraryReview extends UpdateConnector
{
    protected const TABLE_NAME = "itinerary_review";
    protected const ID_COLUMN = "username";
    protected const ID_COLUMN_TYPE = "s";

    /**
     * @var string The code of the reviewed itinerary.
     */
    private $reviewed_itinerary;

    /**
     * UpdateItineraryReview constructor.
     * @param string $username The author of the review.
     * @param string $reviewed_itinerary The code of the reviewed itinerary.
     * @warning If the username or the reviewed_itinerary is not set, the connector will die with an error.
     */
    public function __construct(string $username = null, string $reviewed_itinerary = null)
    {
        if (!isset($username) || !isset($reviewed_itinerary) || $reviewed_itinerary == "" || $username == "") {
            die(json_encode(self::get_false("Some required fields are missing.")));
        }
        parent::__construct(strtolower($username));
        $this->reviewed_itinerary = strtolower($reviewed_itinerary);
    }

    /**
     * Update the object from the database and get the result of the operation.
     * @return string The result of the operation.
     * @see UpdateConnector::get_content()
     */
    public function get_content(): string
    {
        $query = "UPDATE " . $this::TABLE_NAME . " SET ";
        foreach ($this->values_to_update as $key => $value) {
            $query .= $key . " = ?, ";
        }
        $query = substr($query, 0, -2);
        $query .= " WHERE " . $this::ID_COLUMN . " = ? AND reviewed_itinerary = ?";

        try {
            // Put both types and data in the same order, in order to use them in the prepared statement.
            $ordered_types = "";
            $data = array();
            foreach ($this->values_to_update as $key => $value) {
                if (!isset($value)) {
                    throw new InvalidArgumentException("Some required fields are missing!");
                }
                $ordered_types .= $this->types[$key];
                array_push($data, $value);
            }
            $ordered_types .= $this::ID_COLUMN_TYPE . "s";
            array_push($data, $this->id, $this->reviewed_itinerary);

            if (!isset($this->id)) {
                throw new InvalidArgumentException("Some required fields are missing!");
            }

            if ($statement = $this->connection->prepare($query)) {
                $statement->bind_param($ordered_types, ...$data);
                if ($statement->execute()) {
                    $to_return = self::get_true();
                } else {
                    throw new mysqli_sql_exception($statement->error);
                }
            } else {
                throw new mysqli_sql_exception($this->connection->error);
            }
        } catch (Exception $e) {
            $to_return = self::get_false($e->getMessage());
        }

        return json_encode($to_return);
    }
}

$connector = new UpdateItineraryReview($_POST['username'], $_POST['reviewed_itinerary']);
$connector->add_value("feedback", $_POST['feedback'], "i");
$connector->add_value("description", $_POST['description'], "s");
print $connector->get_content();