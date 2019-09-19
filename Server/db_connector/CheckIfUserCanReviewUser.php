<?php

namespace db_connector;

require_once "BooleanConnector.php";

/**
 * Request for control over the existence of a possible connection between the possible reviewer and the possible reviewed.
 * @package db_connector
 */
class CheckIfUserCanReviewUser extends BooleanConnector
{
    /** @var string|null Possible reviewer. */
    private $username;
    /** @var string|null Possible reviewed. */
    private $reviewed_user;


    public function __construct(string $reviewed_user = null, string $username = null)
    {

        $this->username = isset($username) && $username != "" ? strtolower($username) : null;
        $this->reviewed_user = isset($reviewed_user) && $reviewed_user != "" ? strtolower($reviewed_user) : null;
        parent::__construct();
    }

    /**
     * Search the object from the database and get the result of the operation.
     * @return string The result of the operation.
     * @see DatabaseConnector::get_content()
     */
    public function get_content(): string
    {
        $query = "SELECT res.username AS Glob1, ress.username AS Glob2, itinerary.username AS Cic
        FROM reservation AS res, reservation AS ress, itinerary
        WHERE res.username <> ress.username AND itinerary.itinerary_code=res.booked_itinerary AND itinerary.itinerary_code=ress.booked_itinerary AND res.requested_date=ress.requested_date AND res.requested_date<CURRENT_DATE AND
        ((res.username=? AND ress.username=?) OR (res.username=? and itinerary.username=?) OR (res.username=? AND itinerary.username=?))";

        if ($statement = $this->connection->prepare($query)) {
            if (!isset($this->username) || !isset($this->reviewed_user)) {
                die(json_encode(self::get_false("Required fields missing")));
            }
            $statement->bind_param("ssssss", $username, $reviewed_user, $username, $reviewed_user, $reviewed_user, $username);
            if ($statement->execute()) {
                $result = $statement->get_result();
                if ($result->num_rows == 0) {
                    $to_return = self::get_false();
                } else {
                    $to_return = self::get_true();
                }

            } else {
                $to_return = self::get_false($statement->error);
            }
        } else {
            $to_return = self::get_false($this->connection->error);
        }

        return json_encode($to_return);
    }


}

$connector = new CheckIfUserCanReviewUser($_POST['username'], $_POST['reviewed_user']);
print $connector->get_content();