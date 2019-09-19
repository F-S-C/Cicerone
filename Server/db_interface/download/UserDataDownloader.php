<?php

namespace db_interface\download;

use db_connector\download\UserDataDownloader;

require_once "../../db_connector/download/UserDataDownloader.php";

$user = strtolower($_GET['username']);
$pass = $_GET['password'];

if (!isset($user) || !isset($pass)) {
    exit("No credentials given");
}

header("Content-type: text/html");
header("Content-Disposition: attachment;filename=Cicerone_" . $user . "_Data.html");

$downloader = new UserDataDownloader($user, $pass);


print $downloader->get_content();
