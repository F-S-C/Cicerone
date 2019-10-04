<?php

namespace database_connector\controller\update;

use database_connector\controller\request\RequestItinerary;
use Exception;
use InvalidArgumentException;
use mysqli_sql_exception;
use notifications\Firebase;
use notifications\Push;

require_once "/home/fsc/www/database_connector/controller/update/UpdateConnector.php";
require_once "/home/fsc/www/database_connector/controller/request/RequestItinerary.php";
require_once '/home/fsc/www/notifications/Firebase.php';
require_once '/home/fsc/www/notifications/Push.php';

/**
 * Update a itinerary reservation.
 */
class UpdateReservation extends UpdateConnector
{
    protected const TABLE_NAME = "reservation";
    protected const ID_COLUMN = "username";
    protected const ID_COLUMN_TYPE = "s";

    /**
     * @var string The code of the itinerary.
     */
    private $itinerary;

    /**
     * UpdateItineraryReview constructor.
     *
     * @param string $username The author of the review.
     * @param string $itinerary The code of the itinerary.
     * @warning If the username or the itinerary is not set, the connector will die with an error.
     */
    public function __construct(string $username = null, string $itinerary = null)
    {
        if (!isset($username) || !isset($itinerary) || $itinerary == "" || $username == "") {
            die(json_encode(self::get_false("Some required fields are missing.")));
        }
        parent::__construct(strtolower($username));
        $this->itinerary = strtolower($itinerary);
    }

    /**
     * Update the object from the database and get the result of the operation.
     *
     * @return string The result of the operation.
     * @see UpdateConnector::get_content()
     */
    public function get_content(): string
    {
        if (sizeof($this->values_to_update) == 0) {
            return json_encode(self::get_false("No values to update"));
        }

        $query = "UPDATE " . $this::TABLE_NAME . " SET ";
        foreach ($this->values_to_update as $key => $value) {
            $query .= $key . " = ?, ";
        }
        $query = substr($query, 0, -2);
        $query .= " WHERE " . $this::ID_COLUMN . " = ? AND booked_itinerary = ?";

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
            $ordered_types .= $this::ID_COLUMN_TYPE . "i";
            array_push($data, $this->id, $this->itinerary);

            if (!isset($this->id)) {
                throw new InvalidArgumentException("Some required fields are missing!");
            }

            if ($statement = $this->connection->prepare($query)) {
                $statement->bind_param($ordered_types, ...$data);
                if ($statement->execute()) {
                    $to_return = self::get_true();
                    if (isset($this->values_to_update["confirm_date"])) {
                        // Send notification to the globetrotter
                        $firebase = new Firebase();
                        $push = new Push();

                        $itinerary = $this->get_from_connector(new RequestItinerary(null, null, null, null, $this->itinerary))[0];

                        $push->set_title("Reservation request confirmed")
                            ->set_message("Your reservation request to the itinerary \"" . $itinerary["title"] . "\" by \"" . $itinerary["username"]["username"] . "\" has been confirmed!")
                            ->set_payload(Push::create_payload(Push::TO_GLOBETROTTER_CONFIRMED_RESERVATION, array($itinerary["title"], $itinerary["username"]["username"])));

                        $firebase->send_to_topic('globetrotter-' . $this->id, $push);
                    }
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