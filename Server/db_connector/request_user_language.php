<?php
/*
	Project: Cicerone
	Team: FSC
	File description: PHP connector that enables communication between Android 
	and MySQL. This file request data from table 'user_language'.
*/
include('configuration.php');

$query = "SELECT * FROM user_language";
$condition = Array();

// Checks if the variables passed via POST exist and adds them to the query
if(isset($_POST['username']))
{
	array_push($condition, "username = '" . strtolower($_POST['username']) . "'");
}
if(isset($_POST['language_code']))
{
	array_push($condition, "language_code = '" .$_POST['language_code'] . "'");
}

addConditionsToQuery($condition, $query);

// Send the query to MySQL and print received records in JSON
requestData($conn, $query);
?>