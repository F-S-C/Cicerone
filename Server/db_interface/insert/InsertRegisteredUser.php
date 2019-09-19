<?php

namespace db_interface\insert;

use db_connector\insert\InsertRegisteredUser;

require_once "../../db_connector/insert/InsertRegisteredUser.php";

$connector = new InsertRegisteredUser();
$connector->add_value(array(strtolower($_POST['username']),
    $_POST['tax_code'],
    $_POST['name'],
    $_POST['surname'],
    password_hash($_POST['password'], PASSWORD_DEFAULT),
    strtolower($_POST['email']),
    $_POST['user_type'],
    $_POST['cellphone'],
    $_POST['birth_date'],
    $_POST['sex']
));
print $connector->get_content();