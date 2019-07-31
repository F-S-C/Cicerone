<?php

namespace db_connector;

require_once("UpdateConnector.php");

class UpdateRegisteredUser extends UpdateConnector
{
    protected const TABLE_NAME = "registered_user";
    protected const ID_COLUMN = "username";
    protected const ID_COLUMN_TYPE = "s";
}

$connector = new UpdateRegisteredUser($_POST['username']);
$connector->add_value("tax_code", $_POST['tax_code'], "s");
$connector->add_value("name", $_POST['name'], "s");
$connector->add_value("surname", $_POST['surname'], "s");
$connector->add_value("password", password_hash($_POST['password'], PASSWORD_DEFAULT), "s");
$connector->add_value("email", $_POST['email'], "s");
$connector->add_value("user_type", $_POST['user_type'], "i");
$connector->add_value("cellphone", $_POST['cellphone'], "s");
$connector->add_value("birthday", $_POST['birthday'], "s");
$connector->add_value("sex", $_POST['sex'], "s");
print $connector->get_content();