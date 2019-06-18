<?php
/*
	Project: Cicerone
	Team: FSC
	File description: PHP connector that enables communication between Android 
	and MySQL. This file request data from table 'itinerary_review'.
*/
include('configuration.php');

$query = "SELECT * FROM itinerary_review";
$condition = Array();

// Checks if the variables passed via POST exist and adds them to the query
if(isset($_POST['username']))
{
	array_push($condition, "username = '" . strtolower($_POST['username']) . "'");
}
if(isset($_POST['reviewed_itinerary']))
{
	array_push($condition, "reviewed_itinerary = '" . $_POST['reviewed_itinerary'] . "'");
}

addConditionsToQuery($condition, $query);

// Send the query to MySQL and print received records in JSON
requestData($conn, $query);
?>