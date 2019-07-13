<?php
/*
	Project: Cicerone
	Team: FSC
	File description: PHP connector that enables communication between Android 
	and MySQL. This file request data from table 'itinerary'.
*/
include('configuration.php');

$query = "SELECT * FROM itinerary";
$condition = Array();
$bd = $_POST['beginning_date'];
$ed = $_POST['ending_date'];

// Checks if the variables passed via POST exist and adds them to the query
if(isset($_POST['username']))
{
	array_push($condition, "username = '" . $_POST['username'] . "'");
}
if(isset($_POST['location']))
{
	array_push($condition, "location = '" . $_POST['location'] . "'");
}
if(isset($bd) && isset($ed))
{
	$date_condition = "( beginning_date <= " . $bd . " AND ending_date >= " . $bd . ")
	 OR ( beginning_date >= " . $bd . " AND ending_date <= " . $ed . ") OR (beginning_date <= " . $ed . " AND ending_date >= " . $ed . ")";
	array_push($_condition, $_date_condition);
}
else
{
	if(isset($bd))
	{
		array_push($condition, "beginning_date <= '" . $bd . "'");
	}
	if(isset($bd))
	{
		array_push($condition, "ending_date >= '" . $ed . "'");
	}
}
if(isset($_POST['itinerary_code']))
{
	array_push($condition, "itinerary_code = '" . $_POST['itinerary_code'] . "'");
}

addConditionsToQuery($condition, $query);

$query .= " ORDER BY full_price";

// Send the query to MySQL and print received records in JSON
requestData($conn, $query);
?>