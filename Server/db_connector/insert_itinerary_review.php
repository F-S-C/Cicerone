<?php
/*
	Project: Cicerone
	Team: FSC
	File description: PHP connector that enables communication between Android 
	and MySQL. This file insert data into table 'itinerary_review'.
*/
include('configuration.php');

$query = "INSERT INTO itinerary_review (username, reviewed_itinerary, feedback, description) VALUES (";

// Checks if the variables passed via POST exist and adds them to the query
if(isset($_POST['username']) && isset($_POST['reviewed_itinerary']) && isset($_POST['feedback']) && isset($_POST['description']))
	{
		$query .= "'" . strtolower($_POST['username']) . "','" . $_POST['reviewed_itinerary'] . "','" . $_POST['feedback'] . "','" . $_POST['description'] . "')";
		
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