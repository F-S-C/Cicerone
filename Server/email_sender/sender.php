<?php


namespace email_sender;

require '/membri/fsc/email_sender/PHPMailer/src/Exception.php';
require '/membri/fsc/email_sender/PHPMailer/src/PHPMailer.php';
require '/membri/fsc/email_sender/PHPMailer/src/SMTP.php';

use PHPMailer\PHPMailer\PHPMailer;

/**
 * Send an e-mail to a given user.
 */
class Sender
{
    /** @var string The database's server's IP */
    protected const DB_SERVER_NAME = "127.0.0.1";

    /** @var string The database's username. */
    protected const DB_USERNAME = "fsc";

    /** @var string The database's password. */
    protected const DB_P = "89n@W[";

    /** @var string The database's name. */
    protected const DB_NAME = "my_fsc";

    /** @var string The email filename. */
    protected const EMAIL_FILENAME = "./mail.php";

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

    /**
     * @var array The array containing the user and itinerary data to be included in the email.
     */
    private $email_data;

    /**
     * Sender constructor.
     */
    public function __construct()
    {
        if (isset($_POST['username']) && isset($_POST['itinerary_code']) && isset($_POST['recipient_email'])) {
            $this->username = $_POST['username'];
            $this->itinerary_code = $_POST['itinerary_code'];
            $this->recipient_email = $_POST['recipient_email'];
            $this->email_data = $this->getDataFromDB();
            $this->sendEmail();
        } else {
            print '{"result":false, "error":"Missing fields!"}';
        }
    }

    /**
     * Sender destructor.
     */
    public function __destruct()
    {
        //Do nothing
    }

    /**
     * Function that requires a page and converts it into a string passing a list of variables.
     * @param $variablesToMakeLocal array Array of variables to be make local on the email page.
     * @return bool|false|string The e-mail page if successful. False otherwise.
     */
    private function getMailPage($variablesToMakeLocal)
    {
        extract($variablesToMakeLocal);
        if (is_file(self::EMAIL_FILENAME)) {
            ob_start();
            include self::EMAIL_FILENAME;
            return ob_get_clean();
        }
        return false;
    }

    /**
     * Requires the user and itinerary data from the database.
     * @return array Array of string to be inserted in the e-mail page.
     */
    private function getDataFromDB()
    {
        $link = mysqli_connect(self::DB_SERVER_NAME, self::DB_USERNAME, self::DB_P, self::DB_NAME);
        if ($link === false) {
            print '{"result":false,"error":"Could not connect.  ' . mysqli_connect_error() . '"}';
            exit;
        }
        //USER DATA
        $sql = "SELECT * FROM registered_user WHERE username = ?";
        if ($prepared = mysqli_prepare($link, $sql)) {
            mysqli_stmt_bind_param($prepared, "s", $this->username);
            if (mysqli_stmt_execute($prepared)) {
                $result = mysqli_stmt_get_result($prepared);
                if (mysqli_num_rows($result) > 0) {
                    $row = mysqli_fetch_array($result);
                    $user = array('name' => $row['name'], 'surname' => $row['surname'], 'email' => $row['email'], 'cellphone' => $row['cellphone']);
                    mysqli_free_result($result);
                } else {
                    print '{"result":false,"error":"Username not found."}';
                    exit;
                }
            }
        } else {
            print '{"result":false,"error":"Could not able to execute $sql. ' . mysqli_error($link) . '"}';
            exit;
        }

        $sql = "SELECT * FROM itinerary WHERE itinerary_code = ?";
        if ($prepared = mysqli_prepare($link, $sql)) {
            mysqli_stmt_bind_param($prepared, "i", $this->itinerary_code);
            if (mysqli_stmt_execute($prepared)) {
                $result = mysqli_stmt_get_result($prepared);
                if (mysqli_num_rows($result) > 0) {
                    $row = mysqli_fetch_array($result);
                    $itinerary = array('title' => $row['title'], 'location' => $row['location'], 'beginning_date' => $row['beginning_date'], 'ending_date' => $row['ending_date']);
                    mysqli_free_result($result);
                } else {
                    print '{"result":false,"error":"Itinerary not found."}';
                    exit;
                }
            }
        } else {
            print '{"result":false,"error":"Could not able to execute $sql. ' . mysqli_error($link) . '"}';
            exit;
        }

        mysqli_close($link);
        return array_merge($user, $itinerary);
    }

    /*
     * Create the PHPMailer instance and send the e-mail.
     */
    private function sendEmail()
    {
        $mail = new PHPMailer;

        // Decomment if SMTP
//        $mail->isSMTP();                                     // Set mailer to use SMTP
//        $mail->Host = self::SMTP_HOST;                      // Specify main and backup server
//        $mail->Port = self::SMTP_PORT;                                   // Set the SMTP port
//        $mail->SMTPAuth = true;                              // Enable SMTP authentication
//        $mail->Username = self::SMTP_USERNAME;            // SMTP username
//        $mail->Password = self::SMTP_P;             // SMTP password
//        $mail->SMTPSecure = 'tls';                           // Enable encryption

        $mail->From = self::SMTP_USERNAME;
        $mail->FromName = self::SMTP_FROM_NAME;
        $mail->AddAddress($this->recipient_email);  // Destinatario

        $mail->IsHTML(true);                                 // Set email format to HTML

        $mail->Subject = 'Siamo pronti a partire!';
        $mail->Body = $this->getMailPage($this->email_data);
        $mail->AltBody = 'Please use your browser to see the e-mail.';

        if (!$mail->Send()) {
            print '{"result":false, "error":"Mailer Error: ' . $mail->ErrorInfo . '"}';
            exit;
        } else {
            print '{"result":true}';
        }
    }
}

$sender = new Sender();