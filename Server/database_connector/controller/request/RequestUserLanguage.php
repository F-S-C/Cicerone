<?php


namespace database_connector\controller\request;

use database_connector\controller\JsonConnector;

require_once "/home/fsc/www/database_connector/controller/JsonConnector.php";

/**
 * Request the languages that a user speaks.
 */
class RequestUserLanguage extends JsonConnector
{
    /** @var string|null The language code (ISO 639-3). */
    private $language;
    /** @var string|null The user's username. */
    private $username;

    /**
     * RequestUserLanguage constructor.
     * @param string|null $language The language code (ISO 639-3).
     * @param string|null $username The user's username.
     */
    public function __construct(string $language = null, string $username = null)
    {
        $this->language = isset($language) && $language != "" ? $language : null;
        $this->username = isset($username) && $username != "" ? strtolower($username) : null;
        parent::__construct();
    }

    /**
     * @see JsonConnector::fetch_all_rows()
     */
    protected function fetch_all_rows(): array
    {
        $query = "SELECT user_language.username, user_language.language_code, language.language_name FROM user_language INNER JOIN language ON user_language.language_code = language.language_code";

        $conditions = array();
        $data = array();
        $types = "";
        if (isset($this->language)) {
            array_push($conditions, "language_code = ?");
            array_push($data, $this->language);
            $types .= "s";
        }
        if (isset($this->username)) {
            array_push($conditions, "username = ?");
            array_push($data, $this->username);
            $types .= "s";
        }
        $query .= $this->create_SQL_WHERE_clause($conditions);

        return $this->execute_query($query, $data, $types);
    }
}