<?php

namespace database_connector\controller\request;

use database_connector\controller\BooleanConnector;

require_once "/home/fsc/www/database_connector/controller/BooleanConnector.php";

/**
 * Request for control over the existence of a possible connection between the possible reviewer and the possible reviewed.
 */
class CheckIfUserCanReviewUser extends BooleanConnector
{
    /** @var string|null Possible reviewer. */
    private $username;
    /** @var string|null Possible reviewed. */
    private $reviewed_user;


    public function __construct(string $username = null, string $reviewed_user = null)
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
        $query = <<<EOD
SELECT res.username AS Glob1, res.username AS Glob2, itinerary.username AS Cic
FROM reservation AS res, reservation AS ress, itinerary
WHERE 
    (res.username<>ress.username AND res.booked_itinerary=ress.booked_itinerary AND res.requested_date<CURRENT_DATE AND res.booked_itinerary=itinerary.itinerary_code AND (res.username=? AND ress.username=?))
    OR
    (res.booked_itinerary=itinerary.itinerary_code AND res.requested_date<CURRENT_DATE AND ((res.username=? AND itinerary.username=?) OR (res.username=? AND itinerary.username=?)))
EOD;
        if ($statement = $this->connection->prepare($query)) {
            if (!isset($this->username) || !isset($this->reviewed_user)) {
                die(json_encode(self::get_false("Required fields missing")));
            }
            $statement->bind_param("ssssss", $this->username, $this->reviewed_user, $this->username, $this->reviewed_user, $this->reviewed_user, $this->username);
            if ($statement->execute()) {
                if ($statement->get_result()->num_rows == 0) {
                    $to_return = self::get_false("Review not possible");
                } else {
                    $to_return = self::get_true("Review possible");
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