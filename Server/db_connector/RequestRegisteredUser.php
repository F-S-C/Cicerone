<?php

namespace db_connector;

require_once "JsonConnector.php";

/**
 * Get all the information about a user.
 * @package db_connector
 */
class RequestRegisteredUser extends JsonConnector
{
    /** @var string|null The user's username. */
    private $username;
    /** @var string|null The user's email. */
    private $email;

    /**
     * RequestRegisteredUser constructor.
     * @param string|null $username The user's username.
     * @param string|null $email The user's email.
     */
    public function __construct(string $username = null, string $email = null)
    {
        $this->username = isset($username) && $username != "" ? strtolower($username) : null;
        $this->email = isset($email) && $email != "" ? $email : null;
        parent::__construct();
    }

    /**
     * @see JsonConnector::fetch_all_rows()
     */
    protected function fetch_all_rows(): array
    {
        $query = "SELECT * FROM registered_user";
        $conditions = array();
        $data = array();
        $types = "";
        if (isset($this->username)) {
            array_push($conditions, "username = ?");
            array_push($data, $this->username);
            $types .= "s";
        }
        if (!isset($this->username) || $this->username != "admin") {
            array_push($conditions, "username <> 'admin'");
        }
        if (isset($this->email)) {
            array_push($conditions, "email = ?");
            array_push($data, $this->email);
            $types .= "s";
        }
        array_push($conditions, "username <> 'deleted_user'");

        $query .= $this->create_SQL_WHERE_clause($conditions);

        return $this->execute_query($query, $data, $types);
    }
}
