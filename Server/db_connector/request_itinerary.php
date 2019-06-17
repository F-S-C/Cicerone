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

// Create connection
$conn = new mysqli($servername, $username, $password, $db);

// Check connection
if ($conn->connect_error) 
{
    die("Connection failed: " . $conn->connect_error);
}

// Checks if the variables passed via POST exist and adds them to the query
if(isset($_GET['username']))
{
	array_push($condition, "username = '" . $_GET['username'] . "'");
}
if(isset($_GET['location']))
{
	array_push($condition, "location = '" . $_GET['location'] . "'");
}
if(isset($_GET['beginning_date']) && isset($_GET['ending_date']))
{
	$date_condition = "( beginning_date <= " . $_GET['beginning_date'] . " AND ending_date >= " . $_GET['beginning_date'] . ")
	 OR ( beginning_date >= " . $_GET['beginning_date'] . " AND ending_date <= " . $_GET['ending_date'] . ") OR (beginning_date <= " . $_GET['ending_date'] . " AND ending_date >= " . $_GET['ending_date'] . ")";
	array_push($_condition, $_date_condition);
}
else
{
	if(isset($_GET['beginning_date']))
	{
		array_push($condition, "beginning_date <= '" . $_GET['beginning_date'] . "'");
	}
	if(isset($_GET['ending_date']))
	{
		array_push($condition, "ending_date >= '" . $_GET['ending_date'] . "'");
	}
}

if(count($condition) > 0)
	{
		$query = $query . " WHERE ";
		while( count($condition) > 0){
			$query .= $condition[0] ?? '';
			array_shift($condition);
			if(count($condition) > 0)
				$query .= " AND ";
		}
	}

$query .= " ORDER BY price";

// Send the query to MySQL and print received records in JSON
$sth = mysqli_query($conn, $query);
$rows = array();
if ( $sth === false ) 
	{
	  printf("{\"result\":\"false\",\"error\":\"%s\"}", mysqli_error($conn));
	}
else 
	{
		while($r = mysqli_fetch_assoc($sth)) 
		{
			$rows[] = $r;
		}
		print json_encode($rows);
	}
mysqli_close($conn);
?>