<?php

namespace database_connector\controller\insert;

use database_connector\controller\request\RequestItinerary;
use notifications\Firebase;
use notifications\Push;

require_once "/home/fsc/www/database_connector/controller/insert/InsertConnector.php";
require_once "/home/fsc/www/database_connector/controller/request/RequestItinerary.php";
require_once '/home/fsc/www/notifications/Firebase.php';
require_once '/home/fsc/www/notifications/Push.php';

/**
 * Insert a reservation for an itinerary.
 */
class InsertReservation extends InsertConnector
{
    protected const COLUMNS = "username, booked_itinerary, number_of_children, number_of_adults, total, requested_date, forwading_date, confirm_date";
    protected const COLUMNS_TYPE = "siiidsss";
    protected const TABLE_NAME = "reservation";

    /**
     * @see InsertConnector::get_content()
     */
    public function get_content(): string
    {
        $to_return = parent::get_content();

        if (json_decode($to_return, true)[self::RESULT_KEY]) {
            // Send notification to the cicerone
            foreach ($this->values_to_add as $value) {
                $firebase = new Firebase();
                $push = new Push();

                $itinerary = $this->get_from_connector(new RequestItinerary(null, null, null, null, $value[1]))[0];

                $push->set_title("New reservation request")
                    ->set_message("You have a new reservation request on the itinerary \"" . $itinerary["title"] . "\"!")
                    ->set_payload(Push::create_payload(Push::TO_CICERONE_NEW_RESERVATION, array($itinerary["title"])));

                $firebase->send_to_topic('cicerone-' . $itinerary["username"]["username"], $push);
            }
        }

        return $to_return;
    }


}