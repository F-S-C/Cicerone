<?php

namespace db_connector;

require_once("InsertConnector.php");

class InsertRegisteredUser extends InsertConnector
{
    protected const COLUMNS = "username, tax_code, name, surname, password, email, user_type, cellphone, birth_date, sex";
    protected const COLUMNS_TYPE = "ssssssisss";
    protected const TABLE_NAME = "registered_user";
}

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