<?php


namespace email_sender;


use mysqli;

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

    public $username = null;

    public $itinerary_code = null;

    public $usr_email = null;

    public function __construct()
    {
        $this->mysqli = new mysqli(self::DB_HOSTNAME, self::DB_USERNAME, self::DB_P, self::DB_NAME);
        if($this->mysqli->connect_errno > 0){
            die('{"result":false,"error":"Could not connect.  ' . $this->mysqli->connect_error . '"}');
        }
    }

    public function __destruct()
    {
        $this->mysqli->close();
    }

    /**
     * Requires the user from the database.
     * @return array Array of string with user data.
     */
    public function getUserFromDB() {
        if($this->username != null) {
            $sql = "SELECT * FROM registered_user WHERE username = '" . $this->username . "'";
        }elseif ($this->usr_email != null){
            $sql = "SELECT * FROM registered_user WHERE email = '" . $this->usr_email . "'";
        }else{
            die('{"result":false, "error":"DBManager: Cannot execute the query, missing fields!"}');
        }
        if ($result = $this->mysqli->query($sql)) {
            if ($result->num_rows > 0) {
                $row = $result->fetch_assoc();
                $user = array('name' => $row['name'], 'surname' => $row['surname'], 'email' => $row['email'], 'cellphone' => $row['cellphone'], 'username' => $row['username'], 'usrp' => $row['password']);
                $result->free();
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
    public function getItineraryFromDB(){
        if($this->itinerary_code != null) {
            $sql = "SELECT * FROM itinerary WHERE itinerary_code = '" . $this->itinerary_code . "'";
            if ($result = $this->mysqli->query($sql)) {
                if ($result->num_rows > 0) {
                    $row = $result->fetch_assoc();
                    $itinerary = array('title' => $row['title'], 'location' => $row['location'], 'beginning_date' => $row['beginning_date'], 'ending_date' => $row['ending_date']);
                    $result->free();
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

}