<?php
/*
	Project: Cicerone
	Team: FSC
	File description: PHP connector that enables communication between Android 
	and MySQL. This file request data from table 'wishlist'.
*/
include('configuration.php');

$query = "SELECT * FROM wishlist";

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
	$query .= " WHERE username = '" . strtolower($_POST['username']) . "'";
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