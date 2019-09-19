<?php

namespace db_connector\delete;

use db_connector\DeleteConnector;

require_once "../DeleteConnector.php";

/**
 * A connector that deletes an itinerary from a wishlist.
 * @package db_connector
 */
class DeleteWishlist extends DeleteConnector
{
    protected const TABLE_NAME = "wishlist";
    protected const ID_COLUMN = "username";
    protected const ID_COLUMN_TYPE = "s";
    /**
     * @var int The itinerary code that will be removed from the wishlist.
     */
    private $itinerary;

    /**
     * DeleteWishlist constructor.
     * @param string $username The owner of the wishlist.
     * @param int $itinerary The itinerary to be removed.
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
        $query = "DELETE FROM " . $this::TABLE_NAME . " WHERE " . $this::ID_COLUMN . " = ? AND itinerary_in_wishlist = ?";
        if ($statement = $this->connection->prepare($query)) {
            $statement->bind_param($this::ID_COLUMN_TYPE . "i", $this->id, $this->itinerary);
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