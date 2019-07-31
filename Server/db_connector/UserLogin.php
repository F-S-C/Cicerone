<?php

namespace db_connector;

require_once("BooleanConnector.php");

class UserLogin extends BooleanConnector
{
    private $username;
    private $password;

    public function __construct(string $username, string $password)
    {
        if (!isset($username) || !isset($password)) {
            die(json_encode(self::get_false("Some required fields are missing.")));
        }
        $this->username = strtolower($username);
        $this->password = $password;
        parent::__construct();
    }

    public function get_content(): string
    {
        $query = "SELECT password FROM registered_user WHERE username = ?";
        if ($statement = $this->connection->prepare($query)) {
            $statement->bind_param("s", $this->username);
            if ($statement->execute()) {
                if ($row = $statement->get_result()->fetch_assoc()) {
                    $to_return = password_verify($this->password, $row['password']) ? self::get_true() : self::get_false("Wrong username or password");
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

$connector = new UserLogin($_POST['username'], $_POST['password']);
print $connector->get_content();