<?php


namespace email_sender;

require '/home/fsc/www/email_sender/Sender.php';

/**
 * Class Reset: Send a reset e-mail and password to a given user.
 *
 * @package email_sender
 */
class Reset
{
    /** @var string The recipient e-mail. */
    private $email = null;

    /** @var Sender Variable useful for using the Sender class and sending e-mails to a user. */
    private $sender;

    /** @var array Array of strings containing the data of a given user. */
    private $userData;

    /** @var DBManager Variable useful for using the DBManager class to request and write data to the database. */
    private $db_manager;

    /** @var string|null Token used for the reset password process. */
    private $token = null;

    /**
     * Reset constructor.
     */
    public function __construct()
    {
        $this->sender = new Sender();
        $this->db_manager = new DBManager();
        $this->email = htmlspecialchars($_POST['email'] ?? $_GET['email'] ?? null);
        if ($this->email != null) {
            $this->db_manager->usr_email = $this->email;
            $this->userData = $this->db_manager->getUserFromDB();
            $this->token = htmlspecialchars($_GET['token'] ?? null);
            if ($this->token == null) {
                $this->sendTokenViaMail();
            } else {
                $this->resetP();
            }
        } else {
            die('{"result":false,"error":"Reset: Missing email field!"}');
        }
    }

    /**
     * Send an e-mail to the user containing a link to start the password reset process.
     */
    private function sendTokenViaMail(): void
    {
        $this->sender->recipient_email = $this->email;
        $this->sender->email_subject = "Recupera la tua password";
        $this->sender->email_filename = "./mail/resetLink.php";
        $this->sender->email_data = array_merge($this->userData, array("link" => "https://fscgroup.ddns.net/email_sender/Reset.php?token=" . $this->userData['usrp'] . "&email=" . $this->email));
        $this->sender->sendEmail();
    }

    /**
     * Set a password for the user (randomly generated) and send the email with the new password.
     */
    private function resetP(): void
    {
        if (isset($_GET['token'])) {
            $this->db_manager->token = $this->token;
            if ($this->db_manager->checkUserToken()) {
                $newP = $this->randomString();
                if ($this->db_manager->setUserP(password_hash($newP, PASSWORD_DEFAULT))) {
                    $this->sender->recipient_email = $this->email;
                    $this->sender->email_subject = "Ecco la tua password temporanea";
                    $this->sender->email_filename = "./mail/resetPass.php";
                    $this->sender->email_data = array_merge($this->userData, array("p" => $newP));
                    $this->sender->sendEmail();
                    header("Location: https://fscgroup.ddns.net/email_sender/mail/resetCompleted.php");
                }//ELSE ERRORE
            }
        }
    }

    /**
     * Generates a string of 10 characters taken from an internal pattern and returns it to the caller.
     *
     * @return string Randomly generated string.
     */
    private function randomString(): string
    {
        $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ[].,-_#@=?^"*+';
        $charactersLength = strlen($characters);
        $randomString = '';
        for ($i = 0; $i < 10; $i++) {
            $randomString .= $characters[rand(0, $charactersLength - 1)];
        }
        return $randomString;
    }
}

$connector = new Reset();
