<?php


namespace email_sender;

require '/home/fsc/www/email_sender/Sender.php';

/**
 * Class Reset: Send a reset e-mail and password to a given user.
 * @package email_sender
 */
class Reset
{
    /** @var string The recipient e-mail.  */
    private $email = null;

    /** @var Sender Variable useful for using the Sender class and sending e-mails to a user.  */
    private $sender;

    /** @var array Array of strings containing the data of a given user. */
    private $userData;

    /** @var DBManager Variable useful for using the DBManager class to request and write data to the database. */
    private $DBManager;

    /**
     * Reset constructor.
     */
    public function __construct(){
        $this->sender = new Sender();
        $this->DBManager = new DBManager();
        if(isset($_POST['email']) || isset($_GET['email'])){
            $this->email = isset($_GET['email']) ? $_GET['email'] : $_POST['email'];
            $this->DBManager->usr_email = $this->email;
            $this->userData = $this->DBManager->getUserFromDB();
            if(!isset($_GET['token'])){
                $this->sendTokenViaMail();
            }else{
                $this->resetP();
            }
        }else{
            die('{"result":false,"error":"Reset: Missing email field!"}');
        }
    }

    /**
     * Send an e-mail to the user containing a link to start the password reset process.
     */
    private function sendTokenViaMail(){
        $this->sender->recipient_email = $this->email;
        $this->sender->email_subject = "Recupera la tua password";
        $this->sender->email_filename = "./resetLink.php";
        $this->sender->email_data = array_merge($this->userData, array("link" => "https://fscgroup.ddns.net/email_sender/Reset.php?token=" . $this->userData['usrp'] . "&email=" . $this->email));
        $this->sender->sendEmail();
    }

    /**
     * Set a password for the user (randomly generated) and send the email with the new password.
     */
    private function resetP()
    {
        if(isset($_GET['token'])) {
            $this->DBManager->token = $_GET['token'];
            if($this->DBManager->checkUserToken()){
                $newP = $this->randomString();
                if($this->DBManager->setUserP(password_hash($newP, PASSWORD_DEFAULT))) {
                    $this->sender->recipient_email = $this->email;
                    $this->sender->email_subject = "Ecco la tua password temporanea";
                    $this->sender->email_filename = "./resetPass.php";
                    $this->sender->email_data = array_merge($this->userData, array("p" => $newP));
                    $this->sender->sendEmail();
                    header("Location: https://fscgroup.ddns.net/email_sender/resetCompleted.php");
                }//ELSE ERRORE
            }
        }
    }

    /**
     * Generates a string of 10 characters taken from an internal pattern and returns it to the caller.
     * @return string Randomly generated string.
     */
    private function randomString(){
        $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ[].,-_#@=?^"*+';
        $charactersLength = strlen($characters);
        $randomString = '';
        for ($i = 0; $i < 10; $i++) {
            $randomString .= $characters[rand(0, $charactersLength - 1)];
        }
        return $randomString;
    }
}

$reset = new Reset();