<?php
/*
	Project: Cicerone
	Team: FSC
	File description: PHP connector that enables communication between Android 
	and MySQL. This file request data from table 'language'.
*/
include('configuration.php');

$query = "SELECT * FROM language";

// Checks if the variables passed via POST exist and adds them to the query
if(isset($_POST['language_code']))
{
	$query .= " WHERE language_code = '" . $_POST['language_code'] . "'";
}

// Send the query to MySQL and print received records in JSON
requestData($conn, $query);
?>