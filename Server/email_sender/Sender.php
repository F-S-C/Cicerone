<?php


namespace email_sender;

require '/home/fsc/www/email_sender/PHPMailer/src/Exception.php';
require '/home/fsc/www/email_sender/PHPMailer/src/PHPMailer.php';
require '/home/fsc/www/email_sender/PHPMailer/src/SMTP.php';
require '/home/fsc/www/email_sender/DBManager.php';

/**
 * PHPMailer library.
 */

use PHPMailer\PHPMailer\Exception;
use PHPMailer\PHPMailer\PHPMailer;

/**
 * Class Sender: Send e-mail to a given user
 *
 * @package email_sender
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

    /** @var DBManager Variable useful for using the DBManager class to request and write data to the database. */
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
        if (basename($_SERVER['REQUEST_URI'], ".php") == "Sender") {
            $this->setEmail();
            $this->sendEmail();
        }
    }

    /**
     * Function that requires a page and converts it into a string passing a list of variables.
     *
     * @param $variablesToMakeLocal array Array of variables to be make local on the email page.
     * @return bool|string The e-mail page if successful. False otherwise.
     */
    private function getMailPage(array $variablesToMakeLocal)
    {
        extract($variablesToMakeLocal);
        if (is_file($this->email_filename)) {
            ob_start();
            include $this->email_filename;
            return ob_get_clean();
        }
        return false;
    }

    /**
     * Create the PHPMailer instance and send the e-mail.
     */
    public function sendEmail(): void
    {
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
        $mail->Body = $this->getMailPage($this->email_data);
        $mail->AltBody = 'Please use your browser to see the e-mail.';
        try {
            $mail->Send();
            if ($mail->isError()) {
                die('{"result":false, "error":"Sender: Mailer Error: ' . $mail->ErrorInfo . '"}');
            } else {
                print '{"result":true}';
            }
        } catch (Exception $e) {
            die('{"result":false, "error":"Sender: Exception: ' . $e . '"}');
        }
    }

    /**
     * Based on the type value passed in post, the context of the email and its data changes.
     */
    public function setEmail(): void
    {
        $this->recipient_email = htmlspecialchars($_POST['recipient_email'] ?? null);
        $this->db_manager->username = htmlspecialchars($_POST['username'] ?? null);
        $this->db_manager->itinerary_code = htmlspecialchars($_POST['itinerary_code'] ?? null);
        if ($this->recipient_email != null && $this->db_manager->username != null) {
            switch (htmlspecialchars($_POST['type'] ?? null)) {
                case "registrationConfirmed":
                    $this->email_filename = "./mail/registrationConfirmed.php";
                    $this->email_subject = "Registrazione completata!";
                    $this->email_data = $this->db_manager->getUserFromDB();
                    break;
                case "reservationConfirmation":
                    $this->email_filename = "./mail/reservationConfirmation.php";
                    $this->email_subject = "Siamo pronti a partire!";
                    $this->email_data = array_merge($this->db_manager->getUserFromDB(), $this->db_manager->getItineraryFromDB());
                    break;
                case "newItineraryRequest":
                    $this->email_filename = "./mail/newItineraryRequest.php";
                    $this->email_subject = "Hai una nuova richiesta!";
                    $this->email_data = array_merge($this->db_manager->getUserFromDB(), $this->db_manager->getItineraryFromDB());
                    break;
                case "reservationRefuse":
                    $this->email_filename = "./mail/reservationRefuse.php";
                    $this->email_subject = "La tua richiesta non e' stata accettata.";
                    $this->email_data = array_merge($this->db_manager->getUserFromDB(),
                        $this->db_manager->getItineraryFromDB(),
                        array("cicerone_email" => htmlspecialchars($_POST['cicerone_email'] ?? "")));
                    break;
                case "reservationRemove":
                    $this->email_filename = "./mail/reservationRemove.php";
                    $this->email_subject = "Un'utente ha rimosso la sua prenotazione!";
                    $this->db_manager->itinerary_code = htmlspecialchars($_POST['itinerary_code']);
                    $this->email_data = array_merge($this->db_manager->getUserFromDB(), $this->db_manager->getItineraryFromDB(),
                        array("globetrotter_email" => htmlspecialchars($_POST['globetrotter_email'] ?? ""), "globetrotter_name" => htmlspecialchars($_POST['globetrotter_name'] ?? ""), "globetrotter_surname" => htmlspecialchars($_POST['globetrotter_surname'] ?? "")));
                    break;
                case "accountDeleted":
                    $this->email_filename = "./mail/accountDeleted.php";
                    $this->email_subject = "Il tuo account e' stato cancellato correttamente";
                    $this->email_data = array("name" => $this->db_manager->username);
                    break;
                default:
                    die('{"result":false, "error":"Sender: Unknown or missing type!"}');
                    break;
            }
        } else {
            die('{"result":false, "error":"Sender: Missing recipient e-mail or username field!"}');
        }
    }
}

$connector = new Sender();
