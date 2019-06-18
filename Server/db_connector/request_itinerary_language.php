<?php
/*
	Project: Cicerone
	Team: FSC
	File description: PHP connector that enables communication between Android 
	and MySQL. This file request data from table 'itinerary_language'.
*/
include('configuration.php');

$query = "SELECT * FROM itinerary_language";
$condition = Array();

// Checks if the variables passed via POST exist and adds them to the query
if(isset($_POST['language_code']))
{
	array_push($condition, "language_code = '" . $_POST['language_code'] . "'");
}
if(isset($_POST['itinerary_code']))
{
	array_push($condition, "itinerary_code = '" . $_POST['itinerary_code'] . "'");
}

addConditionsToQuery($condition, $query);

// Send the query to MySQL and print received records in JSON
requestData($conn, $query);
?>