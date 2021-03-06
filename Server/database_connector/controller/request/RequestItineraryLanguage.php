<?php


namespace database_connector\controller\request;

use database_connector\controller\JsonConnector;

require_once "/home/fsc/www/database_connector/controller/JsonConnector.php";

/**
 * Request the languages in which an itinerary is available.
 */
class RequestItineraryLanguage extends JsonConnector
{
    /** @var string|null The language that is searched. */
    private $language;
    /** @var string|null The itinerary in which the language has to be searched. */
    private $itinerary;

    /**
     * RequestItineraryLanguage constructor.
     *
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
        $additional_column = isset($this->itinerary) ? "" : "itinerary_code,";
        $query = "SELECT " . $additional_column . "itinerary_language.language_code, language.language_name FROM itinerary_language INNER JOIN language ON itinerary_language.language_code = language.language_code";

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