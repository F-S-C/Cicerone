<?php
/*
	Project: Cicerone
	Team: FSC
	File description: PHP connector that enables communication between Android 
	and MySQL. This file insert data into table 'itinerary_language'.
*/
include('configuration.php');

$query = "INSERT INTO itinerary_language (itinerary_code, language_code) VALUES (";

// Create connection
$conn = new mysqli($servername, $username, $password, $db);

// Check connection
if ($conn->connect_error) 
	{
		die("Connection failed: " . $conn->connect_error);
	}

// Checks if the variables passed via POST exist and adds them to the query
if(isset($_POST['itinerary_code']) && isset($_POST['language_code']))
	{
		$query .= "'" . $_POST['itinerary_code'] . "','" . $_POST['language_code'] . "')";

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