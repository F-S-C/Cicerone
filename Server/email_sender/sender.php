<?php
//ini_set('display_errors', 1); //Debug only!


require './PHPMailer/src/Exception.php';
require './PHPMailer/src/PHPMailer.php';
require './PHPMailer/src/SMTP.php';
require './functions.php';

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

if(isset($_POST['username']) && isset($_POST['itinerary_code']) && isset($_POST['recipient_email'])){
    $username = $_POST['username'];
    $itinerary_code = $_POST['itinerary_code'];
    $recipient_email = $_POST['recipient_email'];
    $data = getDataFromDB($username,$itinerary_code);
}else{
    echo '{"result":false, "error":"Missing fields!"}';
    exit;
}

$mail = new PHPMailer;
$mail->isSMTP();                                     // Set mailer to use SMTP
$mail->Host = 'smtp.gmail.com';                      // Specify main and backup server
$mail->Port = 587;                                   // Set the SMTP port
$mail->SMTPAuth = true;                              // Enable SMTP authentication
$mail->Username = getSMTPData(1);            // SMTP username
$mail->Password = getSMTPData(2);             // SMTP password
$mail->SMTPSecure = 'tls';                           // Enable encryption

$mail->From = getSMTPData(1);
$mail->FromName = getSMTPData(3); 
$mail->AddAddress($recipient_email);  // Destinatario

$mail->IsHTML(true);                                 // Set email format to HTML

$mail->Subject = 'Siamo pronti a partire!';
$mail->Body    = getMailPage('./mail.php', $data);
$mail->AltBody = 'Please use your browser to see the e-mail.';

if(!$mail->Send()) {
   echo '{"result":false, "error":"Mailer Error: ' . $mail->ErrorInfo . '"}';
   exit;
}

echo '{"result":true}';