<?php
/*
	Project: Cicerone
	Team: FSC
	File description: PHP connector that enables communication between Android 
	and MySQL. This file request data from table 'registered_user'.
*/
include('configuration.php');

$query = "SELECT * FROM registered_user";
$condition = Array();

// Checks if the variables passed via POST exist and adds them to the query
if(isset($_POST['username']))
{
	array_push($condition, "username = '" . $_POST['username'] . "'");
}
if(isset($_POST['email']))
{
	array_push($condition, "email = '" . $_POST['email'] . "'");
}

addConditionsToQuery($condition, $query);
// Send the query to MySQL and print received records in JSON
requestData($conn, $query);
?>