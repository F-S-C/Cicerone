<?php
/*
	Project: Cicerone
	Team: FSC
	File description: PHP connector that enables communication between Android 
	and MySQL. This file insert data into table 'report_detail'.
*/
include('configuration.php');

$query = "INSERT INTO report_detail (report_code, report_body, object, state) VALUES (";

// Checks if the variables passed via POST exist and adds them to the query
if(isset($_POST['report_code']) && isset($_POST['report_body']) && isset($_POST['object']) && isset($_POST['state'
]))
	{
		$query .= "'" . strtolower($_POST['report_code']) . "','" . $_POST['report_body'] . "','" . $_POST['object'] . "','" . $_POST['state'] . "')";
		
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