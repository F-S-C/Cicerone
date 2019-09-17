<?php

namespace db_connector;

require_once("RequestUserLanguage.php");

$connector = new RequestItineraryLanguage($_POST['language_code'], $_POST['username']);
print $connector->get_content();