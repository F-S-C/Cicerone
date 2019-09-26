<?php


namespace email_sender;

require '/home/fsc/www/email_sender/sender.php';

class Reset
{

    private $email = null;

    private $sender;

    private $DBManager;

    public function __construct(){
        $this->sender = new Sender();
        $this->DBManager = new DBManager();
        if(!isset($_GET['token'])){
            $this->sendTokenViaMail();
        }else{
            $this->resetPassword();
        }
    }

    public function __destruct(){
        //Do nothing
    }

    private function sendTokenViaMail(){
        if(isset($_POST['email'])){
            $this->email = $_POST['email'];
            $this->DBManager->usr_email = $this->email;
            $userData = $this->DBManager->getUserFromDB();

            $this->sender->recipient_email = $this->email;
            $this->sender->email_subject = "Recupera la tua password";
            $this->sender->email_filename = "./resetLink.php";
            $this->sender->email_data = array_merge($userData, array("link" => "https://fscgroup.ddns.net/email_sender/reset.php?token=" . $userData['usrp'] . "&email=" . $this->email));
            $this->sender->sendEmail();
        }else{
            die('{"result":false,"error":"Reset: Missing email field!"}');
        }
    }

    private function resetPassword()
    {
        echo $_GET['token']; //TODO ATTENZIONE, QUI CREA UNA FUNCTION IN DBMANAGER CHE VERIFICA SE LA PASSWORD E L'EMAIL CORRISPONDINO, SE CORRETTE INVIA LA MAIL CON LA NUOVA PASSWORD GENERATA QUI
    }
}

$reset = new Reset();