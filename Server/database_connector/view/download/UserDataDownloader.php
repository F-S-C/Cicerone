<?php

namespace database_connector\view\download;

use database_connector\controller\download\UserDataDownloader;

require_once "/membri/fsc/database_connector/controller/download/UserDataDownloader.php";

$user = htmlspecialchars(strtolower($_GET['username']));
$pass = $_GET['password'];

if (!isset($user) || !isset($pass)) {
    exit("No credentials given");
}

header("Content-type: text/html");
header("Content-Disposition: attachment;filename=Cicerone_" . $user . "_Data.html");

$downloader = new UserDataDownloader($user, $pass);


print $downloader->get_content();
