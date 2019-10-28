<?php

namespace database_connector\controller\delete;

use database_connector\controller\request\RequestItinerary;
use notifications\Firebase;
use notifications\Push;

require_once "/home/fsc/www/database_connector/controller/delete/DeleteConnector.php";
require_once "/home/fsc/www/database_connector/controller/request/RequestItinerary.php";
require_once '/home/fsc/www/notifications/Firebase.php';
require_once '/home/fsc/www/notifications/Push.php';

/**
 * A connector that deletes a reservation from the reservation table.
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
     * @var bool Whether or not the delete request was made by the cicerone of the itinerary.
     */
    private $deleted_by_cicerone;

    /**
     * DeleteReservation constructor.
     *
     * @param string $username The author of the review.
     * @param int $itinerary The code of the booked itinerary.
     * @param bool $deleted_by_cicerone Whether or not the delete request was made by the cicerone of the itinerary.
     * @warning If the username or the itinerary is not set, the connector will die with an error.
     */
    public function __construct(string $username, int $itinerary, bool $deleted_by_cicerone = false)
    {
        if (!isset($username) || !isset($itinerary) || $itinerary == "" || $username == "") {
            die(json_encode(self::get_false("Some required fields are missing.")));
        }
        parent::__construct(strtolower($username));
        $this->itinerary = $itinerary;
        $this->deleted_by_cicerone = $deleted_by_cicerone;
    }

    /**
     * Delete the object from the database and get the result of the operation.
     *
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

                // Send notification to the cicerone
                $firebase = new Firebase();
                $push = new Push();

                $itinerary = $this->get_from_connector(new RequestItinerary(null, null, null, null, $this->itinerary))[0];

                if ($this->deleted_by_cicerone) {
                    $title = "Reservation request refused";
                    $message = "Your reservation request to the itinerary \"" . $itinerary["title"] . "\" by \"" . $itinerary["username"]["username"] . "\" has been refused.";
                    $payload = Push::create_payload(Push::TO_GLOBETROTTER_REFUSED_RESERVATION, array($itinerary["title"], $itinerary["username"]["username"]));
                    $topic = 'globetrotter-' . $this->id;
                } else {
                    $title = "Reservation deleted";
                    $message = "A reservation to the itinerary \"" . $itinerary["title"] . "\" by \"" . $this->id . "\" has been deleted!";
                    $payload = Push::create_payload(Push::TO_CICERONE_REMOVED_RESERVATION, array($itinerary["title"], $this->id));
                    $topic = 'cicerone-' . $itinerary["username"]["username"];
                }
                
                $push->set_title($title)
                    ->set_message($message)
                    ->set_payload($payload);

                $firebase->send_to_topic($topic, $push);
            } else {
                $to_return = self::get_false($statement->error);
            }
        } else {
            $to_return = self::get_false($this->connection->error);
        }

        return json_encode($to_return);
    }
}