<?php
/*
	Project: Cicerone
	Team: FSC
	File description: PHP connector that enables communication between Android 
	and MySQL. This file delete data from table 'wishlist'.
*/
include('configuration.php');

// Checks if the variables passed via POST exist and adds them to the query
if(isset($_POST['username']) && isset($_POST['itinerary_in_wishlist']))
	{
		$query = "DELETE FROM wishlist WHERE username = '" . strtolower($_POST['username']) . "'" . " AND itinerary_in_wishlist = '" . $_POST['itinerary_in_wishlist'] . "'";
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