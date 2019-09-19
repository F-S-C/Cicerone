<?php

namespace controller\delete;

use database_connector\controller\DeleteConnector;

require_once "/membri/fsc/database_connector/controller/DeleteConnector.php";

/**
 * A connector that deletes an reservation from the reservation table.
 * @package database_connector\controller\delete
 */
class DeleteReservation extends DeleteConnector
{
    protected const TABLE_NAME = "reservation";
    protected const ID_COLUMN = "username";
    protected const ID_COLUMN_TYPE = "s";
    /**
     * @var int The code of the itinerary for which a reservation was requested.
     */
    private $itinerary;

    /**
     * DeleteReservation constructor.
     * @param string $username The author of the review.
     * @param int $itinerary The code of the booked itinerary.
     * @warning If the username or the itinerary is not set, the connector will die with an error.
     */
    public function __construct(string $username = null, int $itinerary = null)
    {
        if (!isset($username) || !isset($itinerary) || $itinerary == "" || $username == "") {
            die(json_encode(self::get_false("Some required fields are missing.")));
        }
        parent::__construct(strtolower($username));
        $this->itinerary = $itinerary;
    }

    /**
     * Delete the object from the database and get the result of the operation.
     * @return string The result of the operation.
     * @see DeleteConnector::get_content()
     */
    public function get_content(): string
    {
        $query = "DELETE FROM " . $this::TABLE_NAME . " WHERE " . $this::ID_COLUMN . " = ? AND booked_itinerary = ?";
        if ($statement = $this->connection->prepare($query)) {
            $statement->bind_param($this::ID_COLUMN_TYPE . "s", $this->id, $this->itinerary);
            if ($statement->execute()) {
                $to_return = self::get_true();
            } else {
                $to_return = self::get_false($statement->error);
            }
        } else {
            $to_return = self::get_false($this->connection->error);
        }

        return json_encode($to_return);
    }
}