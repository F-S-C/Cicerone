<?php

namespace db_connector\delete;

use db_connector\DeleteConnector;

require_once "../DeleteConnector.php";

/**
 * A connector that deletes an review from the user_review table.
 * @package db_connector
 */
class DeleteItineraryReview extends DeleteConnector
{
    protected const TABLE_NAME = "itinerary_review";
    protected const ID_COLUMN = "username";
    protected const ID_COLUMN_TYPE = "s";
    /**
     * @var string The username of the reviewed user that will be updated.
     */
    private $reviewed_itinerary;

    /**
     * DeleteItineraryReview constructor.
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
        $this->reviewed_itinerary = $reviewed_itinerary;
    }

    /**
     * Delete the object from the database and get the result of the operation.
     * @return string The result of the operation.
     * @see DeleteConnector::get_content()
     */
    public function get_content(): string
    {
        $query = "DELETE FROM " . $this::TABLE_NAME . " WHERE " . $this::ID_COLUMN . " = ? AND reviewed_itinerary = ?";
        if ($statement = $this->connection->prepare($query)) {
            $statement->bind_param($this::ID_COLUMN_TYPE . "s", $this->id, $this->reviewed_itinerary);
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