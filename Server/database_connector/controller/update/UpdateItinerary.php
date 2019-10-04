<?php

namespace database_connector\controller\update;

use database_connector\controller\request\RequestItinerary;
use notifications\Firebase;
use notifications\Push;

require_once "/home/fsc/www/database_connector/controller/update/UpdateConnector.php";
require_once "/home/fsc/www/database_connector/controller/request/RequestItinerary.php";
require_once '/home/fsc/www/notifications/Firebase.php';
require_once '/home/fsc/www/notifications/Push.php';

/**
 * Update an itinerary.
 */
class UpdateItinerary extends UpdateConnector
{
    protected const TABLE_NAME = "itinerary";
    protected const ID_COLUMN = "itinerary_code";
    protected const ID_COLUMN_TYPE = "i";

    public function get_content(): string
    {
        $to_return = parent::get_content();
        if (json_decode($to_return, true)[self::RESULT_KEY]) {
            $firebase = new Firebase();
            $push = new Push();

            $itinerary = $this->get_from_connector(new RequestItinerary(null, null, null, null, $this->id))[0];

            $push->set_title("Itinerary updated")
                ->set_message("The itinerary \"" . $itinerary["title"] . "\" by " . $itinerary["username"]["username"] . " has been updated. Be sure to check out its new information.")
                ->set_payload(Push::create_payload(Push::TO_GLOBETROTTER_UPDATED_ITINERARY, array($itinerary["title"], $itinerary["username"]["username"])));

            $firebase->send_to_topic('itinerary-' . $this->id, $push);
        }
        return $to_return;
    }
}