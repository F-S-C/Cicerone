<?php
/*
	Project: Cicerone
	Team: FSC
	File description: PHP connector that enables communication between Android 
	and MySQL. This file request data from table 'wishlist'.
*/
include('configuration.php');

$query = "SELECT * FROM wishlist";

// Checks if the variables passed via POST exist and adds them to the query
if(isset($_POST['username']))
{
	$query .= " WHERE username = '" . strtolower($_POST['username']) . "'";
}

// Send the query to MySQL and print received records in JSON
requestData($conn, $query);
?>