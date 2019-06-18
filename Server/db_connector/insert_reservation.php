<?php
/*
	Project: Cicerone
	Team: FSC
	File description: PHP connector that enables communication between Android 
	and MySQL. This file insert data into table 'reservation'.
*/
include('configuration.php');

$query = "INSERT INTO reservation (username, booked_itinerary, number_of_children, number_of_adults, total, requested_date, forwarding_date, confirmation_date) VALUES (";

// Checks if the variables passed via POST exist and adds them to the query
if(isset($_POST['username']) && isset($_POST['booked_itinerary']) && isset($_POST['number_of_children']) && isset($_POST['number_of_adults']) && isset($_POST['total']) && isset($_POST['requested_date']) && isset($_POST['forwarding_date']) && isset($_POST['confirmation_date']))
	{
		$query .= "'" . strtolower($_POST['username']) . "','" . $_POST['booked_itinerary'] . "','" . $_POST['number_of_children'] . "','" . $_POST['number_of_adults'] . "','" . $_POST['total'] . "','" . $_POST['requested_date'] . "','" . $_POST['forwarding_date'] . "','" . $_POST['confirmation_date'] . "')";
		
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