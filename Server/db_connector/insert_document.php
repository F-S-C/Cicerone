<?php
/*
	Project: Cicerone
	Team: FSC
	File description: PHP connector that enables communication between Android 
	and MySQL. This file insert data into table 'document'.
*/
include('configuration.php');

$query = "INSERT INTO document (username, document_number, document_type, expiry_date) VALUES (";

// Checks if the variables passed via POST exist and adds them to the query
if(isset($_POST['username']) && isset($_POST['document_number']) && isset($_POST['document_type']) && isset($_POST['expiry_date']))
	{
		$query .= strtolower("'" . $_POST['username'] . "','" . $_POST['document_number'] . "','" . $_POST['document_type'] . "','" . $_POST['expiry_date']) . "')";

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