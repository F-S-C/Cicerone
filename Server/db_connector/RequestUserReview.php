<?php

namespace db_connector;

use mysqli_sql_exception;

require_once("JsonConnector.php");

/**
 * Request the reviews written or received by a user.
 * @package db_connector
 */
class RequestUserReview extends JsonConnector
{
    /** @var string|null The username of the author of the reviews. */
    private $author;
    /** @var string|null The reviewed user's username. */
    private $reviewed_user;

    /**
     * RequestUserReview constructor.
     * @param string|null $author The username of the author of the reviews.
     * @param string|null $reviewed_user The reviewed user's username.
     */
    public function __construct(string $author = null, string $reviewed_user = null)
    {
        $this->author = isset($author) && $author != "" ? strtolower($author) : null;
        $this->reviewed_user = isset($reviewed_user) && $reviewed_user != "" ? strtolower($reviewed_user) : null;
        parent::__construct();
    }

    /**
     * @see JsonConnector::fetch_all_rows()
     */
    protected function fetch_all_rows(): array
    {
        $query = "SELECT * FROM user_review";

        $conditions = array();
        $data = array();
        $types = "";
        if (isset($this->author)) {
            array_push($conditions, "username = ?");
            array_push($data, $this->author);
            $types .= "s";
        }
        if (isset($this->reviewed_user)) {
            array_push($conditions, "reviewed_user = ?");
            array_push($data, $this->reviewed_user);
            $types .= "s";
        }
        $query .= $this->create_SQL_WHERE_clause($conditions);

        $to_return = $this->execute_query($query, $data, $types);
        foreach ($to_return as &$row) {
            $parameters = array($row["reviewed_user"]);
            $row["reviewed_user"] = $this->execute_query("SELECT * FROM registered_user WHERE username = ?", $parameters, "s")[0];
        }

        return $to_return;
    }
}

$connector = new RequestUserReview($_POST['username'], $_POST['reviewed_user']);
print $connector->get_content();