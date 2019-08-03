<?php

namespace db_connector;

require_once("DeleteConnector.php");

class DeleteWishlist extends DeleteConnector
{
    protected const TABLE_NAME = "wishlist";
    protected const ID_COLUMN = "username";
    protected const ID_COLUMN_TYPE = "s";
    private $itinerary;

    public function __construct(string $username = null, int $itinerary = null)
    {
        if (!isset($username) || !isset($itinerary) || $itinerary == "" || $username == "") {
            die(json_encode(self::get_false("Some required fields are missing.")));
        }
        parent::__construct(strtolower($username));
        $this->itinerary = $itinerary;
    }

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

$connector = new DeleteWishlist($_POST['username'], $_POST['itinerary_in_wishlist']);
print $connector->get_content();