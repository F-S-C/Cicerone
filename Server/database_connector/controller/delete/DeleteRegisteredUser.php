<?php


namespace database_connector\controller\delete;

use Exception;
use mysqli_sql_exception;

require_once "/home/fsc/www/database_connector/controller/delete/DeleteConnector.php";


/**
 * A connector that deletes a registered user. All the data created by the user
 * won't be deleted, but will be associated with a fake user 'deleted_user'.
 */
class DeleteRegisteredUser extends DeleteConnector
{
    protected const TABLE_NAME = "registered_user";
    protected const ID_COLUMN = "username";
    protected const ID_COLUMN_TYPE = "s";

    /**
     * @see DeleteConnector::get_content()
     */
    public function get_content(): string
    {
        try {
            $this->update_itineraries();
            $this->update_received_reports();
            $this->update_written_reports();
            $this->update_written_user_reviews();
            $this->update_written_itinerary_reviews();
            $this->remove_received_user_reviews();
            $this->remove_documents();
            $this->remove_languages();
            $this->remove_reservations();
            $this->clear_wishlist();
        } catch (Exception $e) {
            return json_encode($this::get_false($e->getMessage()));
        }
        return parent::get_content(); // Delete the user
    }

    /**
     * Remove the reference to the current user from
     * his itineraries.
     */
    private function update_itineraries(): void
    {
        // First, remove the user's itinerary from everyone wishlist
        $query = "DELETE FROM wishlist WHERE itinerary_in_wishlist IN (SELECT itinerary_code FROM itinerary WHERE username = ?)";
        if ($statement = $this->connection->prepare($query)) {
            $statement->bind_param($this::ID_COLUMN_TYPE . "s", $this->id);
            $statement->execute();
        }

        $query = "UPDATE itinerary SET username = 'deleted_user' WHERE username = ?";
        $this->execute_query($query);
    }

    /**
     * Remove the reference to the current user from all the reports
     * that he wrote.
     */
    private function update_written_reports(): void
    {
        $query = "UPDATE report SET username = 'deleted_user' WHERE username = ?";
        $this->execute_query($query);
    }

    /**
     * Remove the reference to the current user from all the reports
     * that he received.
     */
    private function update_received_reports(): void
    {
        $query = "UPDATE report SET reported_user = 'deleted_user' WHERE reported_user = ?";
        $this->execute_query($query);
    }

    /**
     * Remove the reference to the current user from all the reviews
     * to other users that he wrote.
     */
    private function update_written_user_reviews(): void
    {
        $query = "UPDATE user_review SET username = 'deleted_user' WHERE username = ?";
        $this->execute_query($query);
    }

    /**
     * Remove the reference to the current user from all the reviews
     * to itineraries that he wrote.
     */
    private function update_written_itinerary_reviews(): void
    {
        $query = "UPDATE itinerary_review SET username = 'deleted_user' WHERE username = ?";
        $this->execute_query($query);
    }

    /**
     * Remove all the current user's received reviews.
     */
    private function remove_received_user_reviews(): void
    {
        $query = "DELETE FROM user_review WHERE reviewed_user = ?";
        $this->execute_query($query);
    }

    /**
     * Remove all the current user's document.
     */
    private function remove_documents(): void
    {
        $query = "DELETE FROM document WHERE username = ?";
        $this->execute_query($query);
    }

    /**
     * Remove all the current user's languages.
     */
    private function remove_languages(): void
    {
        $query = "DELETE FROM user_language WHERE username = ?";
        $this->execute_query($query);
    }

    /**
     * Remove all the current user's reservations.
     */
    private function remove_reservations(): void
    {
        $query = "DELETE FROM reservation WHERE username = ?";
        $this->execute_query($query);
    }

    /**
     * Clear the current user's wishlist.
     */
    private function clear_wishlist(): void
    {
        $query = "DELETE FROM wishlist WHERE username = ?";
        $this->execute_query($query);
    }


    /**
     * A utility function used to reduce the code duplication.
     * It executes a query.
     *
     * @param string $query The query to be executed as a prepared statement. It must contain a single
     * parameter to be bond with the ID of the deleted user.
     * @param array|null $parameters [optional] This parameter is unused. It must be defined due to the
     * PHP function management system.
     * @param string|null $types [optional] This parameter is unused. It must be defined due to the PHP
     * function management system.
     * @return array If everything was completed, the array will be a boolean result and a message
     * containing the executed query.
     * @throws mysqli_sql_exception A mysqli_sql_exception is thrown if there were some errors in the
     * execution of the query.
     * @see DatabaseConnector::execute_query(), BooleanConnector::get_true(), BooleanConnector::get_false()
     */
    protected function execute_query(string $query, array &$parameters = null, string $types = null): array
    {
        if ($statement = $this->connection->prepare($query)) {
            $statement->bind_param($this::ID_COLUMN_TYPE, $this->id);
            if (!$statement->execute()) {
                throw new mysqli_sql_exception($statement->error);
            }
        } else {
            throw new mysqli_sql_exception($this->connection->error);
        }
        return $this::get_true("Query " . $query . " executed.");
    }


}