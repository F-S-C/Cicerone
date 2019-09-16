<?php

namespace db_connector;

require_once("RequestReport.php");

$connector = new RequestReport($_POST['report_code'], $_POST['reported_user'], $_POST['username']);
print $connector->get_content();