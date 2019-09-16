<?php

namespace db_connector;

require_once("RequestLanguage.php");

$connector = new RequestLanguage($_POST['language_code']);
print $connector->get_content();