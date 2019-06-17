<?php
/*
	Project: Cicerone
	Team: FSC
	File description: PHP connector that enables communication between Android 
	and MySQL. This file insert data into table 'report'.
*/
include('configuration.php');

$query = "INSERT INTO report (username, report_user) VALUES (";

// Create connection
$conn = new mysqli($servername, $username, $password, $db);

// Check connection
if ($conn->connect_error) 
	{
		die("Connection failed: " . $conn->connect_error);
	}

// Checks if the variables passed via POST exist and adds them to the query
if(isset($_POST['username']) && isset($_POST['report_user']))
	{
		$query .= "'" . strtolower($_POST['username']) . "','" . $_POST['report_user'] . "')";
		
		// Send the query to MySQL and print result in JSON
		$sth = mysqli_query($conn, $query);
		if ( $sth === false ) 
		{
		  printf("{\"result\":\"false\",\"error\":\"%s\"}", mysqli_error($conn));
		}
		else 
		{
		  printf("{\"result\":\"true\"}");
		}
	}
else
	{
		printf("{\"result\":\"false\",\"error\":\"Some required fields are missing!\"}");
	}
mysqli_close($conn);
?>