<?php


namespace email_sender;

require '/home/fsc/www/email_sender/PHPMailer/src/Exception.php';
require '/home/fsc/www/email_sender/PHPMailer/src/PHPMailer.php';
require '/home/fsc/www/email_sender/PHPMailer/src/SMTP.php';
require '/home/fsc/www/email_sender/DBManager.php';

use PHPMailer\PHPMailer\Exception;
use PHPMailer\PHPMailer\PHPMailer;

/**
 * Send an e-mail to a given user.
 */
class Sender
{
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

    /** @var array The array containing the user and itinerary data to be included in the email. */
    public $email_data;

    private $db_manager;

    /** @var string The recipient e-mail. */
    public $recipient_email = null;

    /** @var string The email filename. */
    public $email_filename = null;

    /** @var string The email Subject. */
    public $email_subject = null;

    /**
    * Sender constructor.
    */
    public function __construct()
    {
        $this->db_manager = new DBManager();
        if(basename($_SERVER[REQUEST_URI],".php") == "sender") {
            $this->setEmail();
            $this->sendEmail();
        }
    }

    /**
     * Sender destructor.
     */
    public function __destruct()
    {

    }

    /**
     * Function that requires a page and converts it into a string passing a list of variables.
     * @param $variablesToMakeLocal array Array of variables to be make local on the email page.
     * @return bool|string The e-mail page if successful. False otherwise.
     */
    private function getMailPage($variablesToMakeLocal) {
        extract($variablesToMakeLocal);
        if (is_file($this->email_filename)) {
            ob_start();
            include $this->email_filename;
            return ob_get_clean();
        }
        return false;
    }

    /*
     * Create the PHPMailer instance and send the e-mail.
     */
    public function sendEmail(){
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
            if($mail->isError()) {
                die('{"result":false, "error":"Mailer Error: ' . $mail->ErrorInfo . '"}');
            } else {
                print '{"result":true}';
            }
        } catch (Exception $e) {
            die('{"result":false, "error":"Exception: ' . $e . '"}');
        }
    }

    /**
     *
     */
    public function setEmail(){
        if(isset($_POST['recipient_email']) && isset($_POST['username'])) {
            $this->recipient_email = $_POST['recipient_email'];
            $this->db_manager->username = $_POST['username'];
            if (isset($_POST['type'])) {
                switch ($_POST['type']) {
                    case "registrationConfirmed":
                        $this->email_filename = "./registrationConfirmed.php";
                        $this->email_subject = "Registrazione completata!";
                        $this->email_data = $this->db_manager->getUserFromDB();
                        break;
                    case "reservationConfirmation":
                        $this->email_filename = "./reservationConfirmation.php";
                        $this->email_subject = "Siamo pronti a partire!";
                        if(isset($_POST['itinerary_code'])){
                            $this->db_manager->itinerary_code = $_POST['itinerary_code'];
                            $this->email_data = array_merge($this->db_manager->getUserFromDB(), $this->db_manager->getItineraryFromDB());
                        }else{
                            die('{"result":false, "error":"Missing itinerary code field!"}"');
                        }
                        break;
                    case "newItineraryRequest":
                        $this->email_filename = "./newItineraryRequest.php";
                        $this->email_subject = "Hai una nuova richiesta!";
                        if(isset($_POST['itinerary_code'])){
                            $this->db_manager->itinerary_code = $_POST['itinerary_code'];
                            $this->email_data = array_merge($this->db_manager->getUserFromDB(), $this->db_manager->getItineraryFromDB());
                        }else{
                            die('{"result":false, "error":"Missing itinerary code field!"}"');
                        }
                        break;
                    default:
                        die('{"result":false, "error":"Unknown type!"}');
                        break;
                }
            } else {
                die('{"result":false, "error":"Missing type value!"}"');
            }
        }else{
            die('{"result":false, "error":"Missing recipient e-mail or username field!"}');
        }
    }
}

$sender = new Sender();