<?php

namespace database_connector\controller\request;

use database_connector\controller\BooleanConnector;

require_once "/home/fsc/www/database_connector/controller/BooleanConnector.php";
require_once "/home/fsc/www/database_connector/controller/request/RequestRegisteredUser.php";

/**
 * A special connector to log the user in.
 */
class UserLogin extends BooleanConnector
{
    /** @var string The given username. */
    private $username;
    /** @var string The given password. */
    private $password;

    /**
     * UserLogin constructor.
     *
     * @param string $username The given username.
     * @param string $password The given password.
     */
    public function __construct(string $username, string $password)
    {
        if (!isset($username) || !isset($password)) {
            die(json_encode(self::get_false("Some required fields are missing.")));
        }
        $this->username = strtolower($username);
        $this->password = $password;
        parent::__construct();
    }

    /**
     * Checks if the given password is the same as the stored password (using hashes)
     * for the user with the given username. It returns the result of this check.
     *
     * @return string The result of the login.
     * @see BooleanConnector::get_true()
     * @see BooleanConnector::get_false()
     */
    public function get_content(): string
    {
        $query = "SELECT password FROM registered_user WHERE username = ?";
        if ($statement = $this->connection->prepare($query)) {
            $statement->bind_param("s", $this->username);
            if ($statement->execute()) {
                if ($row = $statement->get_result()->fetch_assoc()) {
                    $user = $this->get_from_connector(new RequestRegisteredUser($this->username, null))[0];
                    $to_return = password_verify($this->password, $row['password']) ? self::get_true($user) : self::get_false("Wrong username or password");
                } else {
                    $to_return = self::get_false("Wrong username or password");
                }
            } else {
                $to_return = self::get_false($statement->error);
            }
        } else {
            $to_return = self::get_false($this->connection->error);
        }

        return json_encode($to_return);
    }
}