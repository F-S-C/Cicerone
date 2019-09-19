<?php


namespace database_connector\controller\request;

use database_connector\controller\JsonConnector;

require_once "/membri/fsc/database_connector/controller/JsonConnector.php";

/**
 * Request the languages in which an itinerary is available.
 * @package database_connector\controller\request
 */
class RequestItineraryLanguage extends JsonConnector
{
    /** @var string|null The language that is searched. */
    private $language;
    /** @var string|null The itinerary in which the language has to be searched. */
    private $itinerary;

    /**
     * RequestItineraryLanguage constructor.
     * @param string|null $language The language that is needed.
     * @param string|null $itinerary The itinerary that is needed.
     */
    public function __construct(string $language = null, string $itinerary = null)
    {
        $this->language = isset($language) && $language != "" ? $language : null;
        $this->itinerary = isset($itinerary) && $itinerary != "" ? $itinerary : null;
        parent::__construct();
    }

    /**
     * @see JsonConnector::fetch_all_rows()
     */
    protected function fetch_all_rows(): array
    {
        $query = "SELECT * FROM itinerary_language";

        $conditions = array();
        $data = array();
        $types = "";
        if (isset($this->language)) {
            array_push($conditions, "language_code = ?");
            array_push($data, $this->language);
            $types .= "s";
        }
        if (isset($this->itinerary)) {
            array_push($conditions, "itinerary_code = ?");
            array_push($data, $this->itinerary);
            $types .= "i";
        }
        $query .= $this->create_SQL_WHERE_clause($conditions);


        return $this->execute_query($query, $data, $types);
    }
}