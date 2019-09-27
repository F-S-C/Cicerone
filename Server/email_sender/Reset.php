<?php


namespace email_sender;

require '/home/fsc/www/email_sender/Sender.php';

class Reset
{

    private $email = null;

    private $sender;

    private $userData;

    private $DBManager;

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

    public function __destruct(){
        //Do nothing
    }

    private function sendTokenViaMail(){
        $this->sender->recipient_email = $this->email;
        $this->sender->email_subject = "Recupera la tua password";
        $this->sender->email_filename = "./resetLink.php";
        $this->sender->email_data = array_merge($this->userData, array("link" => "https://fscgroup.ddns.net/email_sender/reset.php?token=" . $this->userData['usrp'] . "&email=" . $this->email));
        $this->sender->sendEmail();
    }

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