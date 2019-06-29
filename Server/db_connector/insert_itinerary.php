<?php
/*
	Project: Cicerone
	Team: FSC
	File description: PHP connector that enables communication between Android 
	and MySQL. This file insert data into table 'itinerary'.
*/
include('configuration.php');

$query = "INSERT INTO itinerary (title, description, beginning_date, ending_date, end_reservations_date,
maximum_partecipants_number, minimum_partecipants_number, location, repetitions_per_day, duration, username, full_price, reduced_price) VALUES (";
//TODO IMAGE_URL INTO QUERY

// Checks if the variables passed via POST exist and adds them to the query
if(isset($_POST['title']) && isset($_POST['description']) && isset($_POST['beginning_date']) && isset($_POST['ending_date']) && isset($_POST['end_reservations_date']) && isset($_POST['maximum_partecipants_number']) && isset($_POST['minimum_partecipants_number']) && isset($_POST['location']) && isset($_POST['repetitions_per_day']) && isset($_POST['duration']) && isset($_POST['username']) && isset($_POST['full_price']) && isset($_POST['reduced_price']))
	{
		$query .= "'" . $_POST['title'] . "','" . $_POST['description'] . "','" . $_POST['beginning_date'] . "','" . $_POST['ending_date'] . "','" . $_POST['end_reservations_date'] . "','" . $_POST['maximum_partecipants_number'] . "','" . $_POST['minimum_partecipants_number'] . "','" . $_POST['location'] . "','" . $_POST['repetitions_per_day'] . "','" . $_POST['duration'] . "','" . strtolower($_POST['username']) . "','" . $_POST['full_price'] . "','" . $_POST['reduced_price'] . "')";
		
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