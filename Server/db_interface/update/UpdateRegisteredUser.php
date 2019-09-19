<?php

namespace db_interface\update;

use db_connector\update\UpdateRegisteredUser;

require_once "../../db_connector/update/UpdateRegisteredUser.php";

$connector = new UpdateRegisteredUser($_POST['username']);
$connector->add_value("tax_code", $_POST['tax_code'], "s");
$connector->add_value("name", $_POST['name'], "s");
$connector->add_value("surname", $_POST['surname'], "s");
$connector->add_value("password", password_hash($_POST['password'], PASSWORD_DEFAULT), "s");
$connector->add_value("email", $_POST['email'], "s");
$connector->add_value("user_type", $_POST['user_type'], "i");
$connector->add_value("cellphone", $_POST['cellphone'], "s");
$connector->add_value("birth_date", $_POST['birth_date'], "s");
$connector->add_value("sex", $_POST['sex'], "s");
print $connector->get_content();