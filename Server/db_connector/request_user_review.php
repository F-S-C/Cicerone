<?php
/*
	Project: Cicerone
	Team: FSC
	File description: PHP connector that enables communication between Android 
	and MySQL. This file request data from table 'user_review'.
*/
include('configuration.php');

$query = "SELECT * FROM user_review";
$condition = Array();

// Create connection
$conn = new mysqli($servername, $username, $password, $db);

// Check connection
if ($conn->connect_error) 
{
    die("Connection failed: " . $conn->connect_error);
}

// Checks if the variables passed via POST exist and adds them to the query
if(isset($_POST['username']))
{
	array_push($condition, "username = '" . strtolower($_POST['username']) . "'");
}
if(isset($_POST['reviewed_itinerary']))
{
	array_push($condition, "reviewed_user = '" . strtolower($_POST['reviewed_user']) . "'");
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