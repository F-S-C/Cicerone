<?php


namespace email_sender;

/**
 * mysqli library.
 */
use mysqli;
use mysqli_result;

/**
 * Class DBManager: Requires and writing data to the database.
 * @package email_sender
 */
class DBManager
{
    /** @var string The database's server's IP */
    protected const DB_HOSTNAME = "127.0.0.1";

    /** @var string The database's username. */
    protected const DB_USERNAME = "fsc";

    /** @var string The database's password. */
    protected const DB_P = "89n@W[";

    /** @var string The database's name. */
    protected const DB_NAME = "cicerone";

    /** @var mysqli The mysql variable used for the connection to the database. */
    private $mysqli = null;

    /** @var string The username that can be searched in the database. */
    public $username = null;

    /** @var integer The itinerary that can be searched in the database. */
    public $itinerary_code = null;

    /** @var string The email that can be searched in the database.*/
    public $usr_email = null;

    /** @var string The token that is searched with the email to verify that they are correct.*/
    public $token = null;

    /**
     * DBManager constructor. Connect to the database.
     */
    public function __construct()
    {
        $this->mysqli = new mysqli(self::DB_HOSTNAME, self::DB_USERNAME, self::DB_P, self::DB_NAME);
        if($this->mysqli->connect_errno > 0){
            die('{"result":false,"error":"Could not connect.  ' . $this->mysqli->connect_error . '"}');
        }
    }

    /**
     * DBManager destructor. Close the connection with the database.
     */
    public function __destruct()
    {
        $this->mysqli->close();
    }

    /**
     * Requires the user from the database.
     * @return array Array of string with user data.
     */
    public function getUserFromDB() :array {
        if($this->username != null) {
            $sql = "SELECT * FROM registered_user WHERE username = ?";
            $stmt = $this->mysqli->prepare($sql);
            $stmt->bind_param("s",$this->username);
        }elseif ($this->usr_email != null){
            $sql = "SELECT * FROM registered_user WHERE email = ?";
            $stmt = $this->mysqli->prepare($sql);
            $stmt->bind_param("s",$this->usr_email);
        }else{
            die('{"result":false, "error":"DBManager: Cannot execute the query, missing fields!"}');
        }
        if ($stmt->execute()) {
            $result = $stmt->get_result();
            if ($result->num_rows > 0) {
                $row = $result->fetch_assoc();
                $user = array('name' => $row['name'], 'surname' => $row['surname'], 'email' => $row['email'], 'cellphone' => $row['cellphone'], 'username' => $row['username'], 'usrp' => $row['password'], 'sex' => $row['sex']);
                $result->free_result();
                $stmt->close();
                return $user;
            } else {
                die('{"result":false,"error":"Username not found."}');
            }
        } else {
            die('{"result":false,"error":"Could not able to execute query. ' . $this->mysqli->error . '"}');
        }
    }

    /**
     * Requires the itinerary from the database.
     * @return array Array of string with itinerary data.
     */
    public function getItineraryFromDB():array{
        if($this->itinerary_code != null) {
            $sql = "SELECT * FROM itinerary WHERE itinerary_code = ?";
            $stmt = $this->mysqli->prepare($sql);
            $stmt->bind_param("i",$this->itinerary_code);
            if ($stmt->execute()) {
                $result = $stmt->get_result();
                if ($result->num_rows > 0) {
                    $row = $result->fetch_assoc();
                    $itinerary = array('title' => $row['title'], 'location' => $row['location'], 'beginning_date' => $row['beginning_date'], 'ending_date' => $row['ending_date']);
                    $result->free_result();
                    $stmt->close();
                    return $itinerary;
                } else {
                    die('{"result":false,"error":"Itinerary not found."}');
                }
            } else {
                die('{"result":false,"error":"Could not able to execute $sql. ' . $this->mysqli->error . '"}');
            }
        } else {
            die('{"result":false,"error":"Missing itinerary field!"}');
        }
    }

    /**
     * Check that the combination email / password is correct.
     * The password, being encrypted, is called "token".
     * @return bool True if the data is correct. False otherwise.
     */
        public function checkUserToken():bool {
        if($this->token != null && $this->usr_email != null) {
            $sql = "SELECT * FROM registered_user WHERE email = ? AND password = ?";
            $stmt = $this->mysqli->prepare($sql);
            $stmt->bind_param("ss",$this->usr_email, $this->token);
            if ($stmt->execute()) {
                $stmt->store_result();
                $numRows = $stmt->num_rows;
                $stmt->close();
                return ($numRows > 0);
            } else {
                die('{"result":false,"error":"Could not able to execute query. ' . $this->mysqli->error . '"}');
            }
        } else {
            die('{"result":false, "error":"DBManager: Cannot execute the query, missing email and token!"}');
        }
    }

    /**
     * Set a user's password.
     *
     * @param $p string The password to set.
     * @return false|mysqli_result False if the query failed. mysqli_result otherwise.
     */
    public function setUserP(string $p){
        $sql = "UPDATE registered_user SET password = ? WHERE email = ?";
        $stmt = $this->mysqli->prepare($sql);
        $stmt->bind_param("ss",$p,$this->usr_email);
        $value = $stmt->execute();
        $stmt->close();
        return $value;
    }
}