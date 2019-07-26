<?php
/*
	Project: Cicerone
	Team: FSC
	File description: PHP connector that enables communication between Android 
	and MySQL. This file request data from table 'wishlist'.
*/
include('configuration.php');

$query = "SELECT * FROM wishlist";
$condition = Array();

// Checks if the variables passed via POST exist and adds them to the query
if(isset($_POST['username']))
{
	array_push($condition, "username = '" . strtolower($_POST['username']) . "'");
}
if(isset($_POST['itinerary_in_wishlist']))
{
	array_push($condition, "itinerary_in_wishlist = '" . $_POST['itinerary_in_wishlist'] . "'");
}

addConditionsToQuery($condition, $query);

// Send the query to MySQL and print received records in JSON
requestData($conn, $query);
?>