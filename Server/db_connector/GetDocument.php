<?php

namespace db_connector;

require_once("RequestDocument.php");

$connector = new RequestDocument($_POST['username']);
print $connector->get_content();