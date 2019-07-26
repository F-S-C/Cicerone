<?php
/*
	Project: Cicerone
	Team: FSC
	File description: PHP connector that enables communication between Android 
	and MySQL. This file request data from table 'wishlist'.
*/
include('configuration.php');

$query = "SELECT * FROM wishlist,itinerary WHERE wishlist.itinerary_in_wishlist = itinerary.itinerary_code";

// Checks if the variables passed via POST exist and adds them to the query
if(isset($_POST['username']))
{
	$query .= " AND wishlist.username = '" . strtolower($_POST['username']) . "'";
}

// Send the query to MySQL and print received records in JSON
requestData($conn, $query);
?>