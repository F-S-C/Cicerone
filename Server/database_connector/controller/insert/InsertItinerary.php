<?php

namespace database_connector\controller\insert;

require_once "/home/fsc/www/database_connector/controller/insert/InsertConnector.php";
require_once "/home/fsc/www/database_connector/controller/insert/InsertItineraryLanguage.php";

/**
 * Insert an itinerary.
 */
class InsertItinerary extends InsertConnector
{
    protected const COLUMNS = "title, description, beginning_date, ending_date, end_reservations_date, maximum_participants_number, minimum_participants_number, location, repetitions_per_day, duration, username, full_price, reduced_price, image_url";
    protected const COLUMNS_TYPE = "sssssiisissdds";
    protected const TABLE_NAME = "itinerary";

    public function get_content(): string
    {
        if (isset($this->values_to_add["languages"])) {
            $languages = json_decode($this->values_to_add["languages"], true);
            unset($this->values_to_add["languages"]);
        } else {
            $languages = null;
        }

        $to_return = parent::get_content();

        if (json_decode($to_return, true)[self::RESULT_KEY]) {
            $id = $this->connection->insert_id;
            if (isset($languages)) {
                $language_connector = new InsertItineraryLanguage();
                foreach ($languages as $language) {
                    $language_connector->add_value(array($id, $language["language_code"]));
                }
                $language_connector->get_content();
            }
        }

        return $to_return;
    }
}