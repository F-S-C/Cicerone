<?php

namespace db_connector;

use mysqli_sql_exception;

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

        if ($statement = $this->connection->prepare($query)) {
            if (isset($this->owner)) {
                $statement->bind_param("s", $this->owner);
            }
            if ($statement->execute()) {
                $to_return = $statement->get_result()->fetch_all(MYSQLI_ASSOC);
            } else {
                throw new mysqli_sql_exception($statement->error);
            }
        } else {
            throw new mysqli_sql_exception($this->connection->error);
        }

        return $to_return;
    }
}

$connector = new RequestDocument($_POST['username']);
print $connector->get_content();