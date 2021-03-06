<?php

namespace database_connector\controller\request;

use database_connector\controller\JsonConnector;

require_once "/home/fsc/www/database_connector/controller/JsonConnector.php";
require_once "/home/fsc/www/database_connector/controller/request/RequestDocument.php";
require_once "/home/fsc/www/database_connector/controller/request/RequestUserLanguage.php";

/**
 * Get all the information about a user.
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
        } else {
            array_push($conditions, "username <> 'admin'");
            array_push($conditions, "username <> 'deleted_user'");
        }
        if (isset($this->email)) {
            array_push($conditions, "email = ?");
            array_push($data, $this->email);
            $types .= "s";
        }

        $query .= $this->create_SQL_WHERE_clause($conditions);

        $to_return = $this->execute_query($query, $data, $types);
        foreach ($to_return as &$row) {
            $row["document"] = $this->get_from_connector(new RequestDocument($row["username"]))[0];
            unset($row["document"]["username"]);
            $row["languages"] = $this->get_from_connector(new RequestUserLanguage(null, $row["username"]));
        }
        return $to_return;
    }
}
