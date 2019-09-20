<?php

namespace database_connector\controller\update;

use Exception;
use InvalidArgumentException;
use mysqli_sql_exception;

require_once "/membri/fsc/database_connector/controller/update/UpdateConnector.php";

/**
 * Update a user's review.
 */
class UpdateUserReview extends UpdateConnector
{
    protected const TABLE_NAME = "user_review";
    protected const ID_COLUMN = "username";
    protected const ID_COLUMN_TYPE = "s";

    /**
     * @var string The username of the reviewed user that will be updated.
     */
    private $reviewed_user;

    /**
     * UpdateUserReview constructor.
     * @param string $username The author of the review.
     * @param string $reviewed_user The username of the reviewed user that will be updated.
     * @warning If the username or the reviewed_user is not set, the connector will die with an error.
     */
    public function __construct(string $username = null, string $reviewed_user = null)
    {
        if (!isset($username) || !isset($reviewed_user) || $reviewed_user == "" || $username == "") {
            die(json_encode(self::get_false("Some required fields are missing.")));
        }
        parent::__construct(strtolower($username));
        $this->reviewed_user = strtolower($reviewed_user);
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
        $query .= " WHERE " . $this::ID_COLUMN . " = ? AND reviewed_user = ?";

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
            array_push($data, $this->id, $this->reviewed_user);

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