<?php
/*
	Project: Cicerone
	Team: FSC
	File description: PHP connector that enables communication between Android 
	and MySQL. This file delete data from table 'itinerary'.
*/
include('configuration.php');

// Checks if the variables passed via POST exist and adds them to the query
if(isset($_POST['itinerary_code']))
	{
		$query = "DELETE FROM itinerary WHERE itinerary_code = '" . $_POST['itinerary_code'] . "'";
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
?>