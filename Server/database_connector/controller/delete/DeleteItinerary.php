<?php

namespace database_connector\controller\delete;

use database_connector\controller\request\RequestItinerary;
use database_connector\controller\request\RequestReservation;
use notifications\Firebase;
use notifications\Push;

require_once "/home/fsc/www/database_connector/controller/delete/DeleteConnector.php";
require_once "/home/fsc/www/database_connector/controller/request/RequestReservation.php";
require_once "/home/fsc/www/database_connector/controller/delete/DeleteReservation.php";
require_once "/home/fsc/www/database_connector/controller/request/RequestItinerary.php";
require_once '/home/fsc/www/notifications/Firebase.php';
require_once '/home/fsc/www/notifications/Push.php';

/**
 * A connector that deletes an itinerary.
 */
class DeleteItinerary extends DeleteConnector
{
    protected const TABLE_NAME = "itinerary";
    protected const ID_COLUMN = "itinerary_code";
    protected const ID_COLUMN_TYPE = "i";

    /**
     * @see DeleteConnector::get_content()
     */
    public function get_content(): string
    {
        // First, delete all the reservations for the itinerary
        $reservations = $this->get_from_connector(new RequestReservation(null, $this->id));
        foreach ($reservations as $reservation) {
            $this->get_from_connector(new DeleteReservation($reservation["username"]["username"], $this->id));
        }

        // Then delete the itinerary
        $to_return = parent::get_content();

        if (json_decode($to_return, true)[self::RESULT_KEY]) {
            $firebase = new Firebase();
            $push = new Push();

            $itinerary = $this->get_from_connector(new RequestItinerary(null, null, null, null, $this->id))[0];

            $push->set_title("Itinerary deleted")
                ->set_message("The itinerary \"" . $itinerary["title"] . "\" by " . $itinerary["username"]["username"] . " has been deleted.")
                ->set_payload(Push::create_payload(Push::TO_GLOBETROTTER_DELETED_ITINERARY, array($itinerary["title"], $itinerary["username"]["username"])));

            $firebase->send_to_topic('itinerary-' . $this->id, $push);
        }

        return $to_return;
    }
}