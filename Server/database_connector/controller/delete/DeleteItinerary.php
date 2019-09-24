<?php

namespace database_connector\controller\delete;

use database_connector\controller\request\RequestReservation;

require_once "/home/fsc/www/database_connector/controller/delete/DeleteConnector.php";
require_once "/home/fsc/www/database_connector/controller/request/RequestReservation.php";
require_once "/home/fsc/www/database_connector/controller/delete/DeleteReservation.php";

/**
 * A connector that deletes an itinerary.
 */
class DeleteItinerary extends DeleteConnector
{
    protected const TABLE_NAME = "itinerary";
    protected const ID_COLUMN = "itinerary_code";
    protected const ID_COLUMN_TYPE = "i";

    public function get_content(): string
    {
        // First, delete all the reservations for the itinerary
        $reservations = $this->get_from_connector(new RequestReservation(null, $this->id));
        foreach ($reservations as $reservation) {
            $this->get_from_connector(new DeleteReservation($reservation["username"]["username"], $this->id));
        }

        // Then delete the itinerary
        return parent::get_content();
    }
}