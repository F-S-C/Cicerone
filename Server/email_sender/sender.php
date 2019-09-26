<?php


namespace email_sender;

require '/home/fsc/www/email_sender/PHPMailer/src/Exception.php';
require '/home/fsc/www/email_sender/PHPMailer/src/PHPMailer.php';
require '/home/fsc/www/email_sender/PHPMailer/src/SMTP.php';

use mysqli;
use PHPMailer\PHPMailer\Exception;
use PHPMailer\PHPMailer\PHPMailer;

/**
 * Send an e-mail to a given user.
 */
class Sender
{
    /** @var string The database's server's IP */
    protected const DB_HOSTNAME = "127.0.0.1";
    
    /** @var string The database's username. */
    protected const DB_USERNAME = "fsc";

    /** @var string The database's password. */
    protected const DB_P = "89n@W[";

    /** @var string The database's name. */
    protected const DB_NAME = "cicerone";

    /** @var string The SMTP host. */
    protected const SMTP_HOST = "smtp.gmail.com";

    /** @var int The SMTP port. */
    protected const SMTP_PORT = 587;

    /** @var string The SMTP username. */
    protected const SMTP_USERNAME = "noreply.cicerone.app@gmail.com";

    /** @var string The SMTP password. */
    protected const SMTP_P = "X36Y$^8s6u";

    /** @var string The SMTP sender's name. */
    protected const SMTP_FROM_NAME = "Cicerone";

    /** @var string The username of the recipient. */
    private $username = null;

    /** @var int The itinerary code */
    private $itinerary_code = null;

    /** @var string The recipient e-mail. */
    private $recipient_email = null;

    /** @var array The array containing the user and itinerary data to be included in the email. */
    private $email_data;

    /** @var mysqli The mysql variable used for the connection to the database. */
    private $mysqli = null;

    /** @var string The email filename. */
    private $email_name = null;

    /** @var string The email Subject. */
    private $email_subject = null;

    /**
    * Sender constructor.
    */
    public function __construct()
    {
        if(isset($_POST['recipient_email'])){
            $this->recipient_email = $_POST['recipient_email'];
            $this->mysqli = new mysqli(self::DB_HOSTNAME, self::DB_USERNAME, self::DB_P, self::DB_NAME);
            if($this->mysqli->connect_errno > 0){
                die('{"result":false,"error":"Could not connect.  ' . $this->mysqli->connect_error . '"}');
            }else{
                $this->setEmail();
            }
        }else{
            die('{"result":false, "error":"Missing recipient e-mail field!"}');
        }
    }

    /**
     * Sender destructor.
     */
    public function __destruct()
    {
        $this->mysqli->close();
    }

    /**
     * Function that requires a page and converts it into a string passing a list of variables.
     * @param $variablesToMakeLocal array Array of variables to be make local on the email page.
     * @return bool|string The e-mail page if successful. False otherwise.
     */
    private function getMailPage($variablesToMakeLocal) {
        extract($variablesToMakeLocal);
        if (is_file($this->email_name)) {
            ob_start();
            include $this->email_name;
            return ob_get_clean();
        }
        return false;
    }

    /**
     * Requires the user from the database.
     * @return array Array of string to be inserted in the e-mail page.
     */
    private function getUserFromDB() {
        if(isset($_POST['username'])) {
            $this->username = $_POST['username'];
            $sql = "SELECT * FROM registered_user WHERE username = '" . $this->username . "'";
            if ($result = $this->mysqli->query($sql)) {
                if ($result->num_rows > 0) {
                    $row = $result->fetch_assoc();
                    $user = array('name' => $row['name'], 'surname' => $row['surname'], 'email' => $row['email'], 'cellphone' => $row['cellphone'], 'username' => $row['username']);
                    $result->free();
                    return $user;
                } else {
                    die('{"result":false,"error":"Username not found."}');
                }
            } else {
                die('{"result":false,"error":"Could not able to execute $sql. ' . $this->mysqli->error . '"}');
            }
        }else{
            die('{"result":false, "error":"Missing username field!"}');
        }
    }

    /**
     * Requires the itinerary from the database.
     * @return array Array of string to be inserted in the e-mail page.
     */
    private function getItineraryFromDB(){
        if(isset($_POST['itinerary_code'])) {
            $this->itinerary_code = $_POST['itinerary_code'];
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
        }else{
            die('{"result":false,"error":"Missing itinerary field!"}');
        }
    }

    /*
     * Create the PHPMailer instance and send the e-mail.
     */
    private function sendEmail(){
        $mail = new PHPMailer;

        $mail->isSMTP();                                     // Set mailer to use SMTP
        $mail->Host = self::SMTP_HOST;                      // Specify main and backup server
        $mail->Port = self::SMTP_PORT;                                   // Set the SMTP port
        $mail->SMTPAuth = true;                              // Enable SMTP authentication
        $mail->Username = self::SMTP_USERNAME;            // SMTP username
        $mail->Password = self::SMTP_P;             // SMTP password
        $mail->SMTPSecure = 'tls';                           // Enable encryption

        $mail->From = self::SMTP_USERNAME;
        $mail->FromName = self::SMTP_FROM_NAME;
        $mail->AddAddress($this->recipient_email);  // Destinatario

        $mail->IsHTML(true);                                 // Set email format to HTML

        $mail->Subject = $this->email_subject;
        $mail->Body    = $this->getMailPage($this->email_data);
        $mail->AltBody = 'Please use your browser to see the e-mail.';

        try {
            $mail->Send();
            print '{"result":true}';
        } catch (Exception $e) {
            die('{"result":false, "error":"Mailer Error: ' . $mail->ErrorInfo . '"}');
        }
    }

    /**
     *
     */
    private function setEmail(){
        if(isset($_POST['type'])){
            switch($_POST['type']){
                case "registrationConfirmed":
                    $this->email_name = "./registrationConfirmed.php";
                    $this->email_subject = "Registrazione completata!";
                    $this->email_data = $this->getUserFromDB();
                    $this->sendEmail();
                    break;
                case "reservationConfirmation":
                    $this->email_name = "./reservationConfirmation.php";
                    $this->email_subject = "Siamo pronti a partire!";
                    $this->email_data = array_merge($this->getUserFromDB(), $this->getItineraryFromDB());
                    $this->sendEmail();
                    break;
                default:
                    $this->mysqli->close();
                    die('{"result":false, "error":"Unknown type!"}');
                    break;
            }
            $this->mysqli->close();
        }else{
            die('{"result":false, "error":"Missing type value!}"');
        }
    }
}

$sender = new Sender();