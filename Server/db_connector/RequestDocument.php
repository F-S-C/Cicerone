<?php

namespace db_connector;

require_once("JsonConnector.php");

/**
 * Request all the documents for a user.
 * @package db_connector
 */
class RequestDocument extends JsonConnector
{
    /** @var string|null The owner of the documents. */
    private $owner;

    /**
     * RequestDocument constructor.
     * @param string|null $owner The owner of the documents.
     */
    public function __construct(string $owner = null)
    {
        $this->owner = isset($owner) && $owner != "" ? strtolower($owner) : null;
        parent::__construct();
    }

    /**
     * @see JsonConnector::fetch_all_rows()
     */
    protected function fetch_all_rows(): array
    {
        $query = "SELECT * FROM document";
        if (isset($this->owner)) {
            $query .= " WHERE username = ?";
        }

        $parameters = isset($this->owner) ? array($this->owner) : array();
        return $this->$this->execute_query($query, $parameters, isset($this->owner) ? "s" : "");
    }
}