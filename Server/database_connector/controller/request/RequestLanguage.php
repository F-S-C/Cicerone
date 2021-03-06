<?php

namespace database_connector\controller\request;

use database_connector\controller\JsonConnector;

require_once "/home/fsc/www/database_connector/controller/JsonConnector.php";

/**
 * Request the languages.
 */
class RequestLanguage extends JsonConnector
{
    /** @var string|null The code of the language (ISO 639-3). */
    private $code;

    /**
     * RequestLanguage constructor.
     * @param string|null $code The code of the language (ISO 639-3).
     */
    public function __construct(string $code = null)
    {
        $this->code = isset($code) && $code != "" ? $code : null;
        parent::__construct();
    }

    /**
     * @see JsonConnector::fetch_all_rows()
     */
    protected function fetch_all_rows(): array
    {
        $query = "SELECT * FROM language";
        if (isset($this->code)) {
            $query .= " WHERE language_code = ?";
        }

        $data = array($this->code);

        return $this->execute_query($query, $data, "s");
    }


}