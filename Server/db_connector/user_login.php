<?php 
/*
	Project: Cicerone
	Team: FSC
	File description: PHP connector that enables communication between Android 
	and MySQL. This file check the username and password provided by the user 
	for login.
*/
include('configuration.php');

$query = "SELECT password FROM registered_user ";
$condition = Array();

// Create connection
$conn = new mysqli($servername, $username, $password, $db);

// Check connection
if ($conn->connect_error) 
	{
		die("Connection failed: " . $conn->connect_error);
	}

// Checks if the variables passed via POST exist and adds them to the query
if(isset($_POST['username']) && isset($_POST['password']))
	{
		$query .= "WHERE username = '" .strtolower($_POST['username']). "'";
		
		// Send the query to MySQL and print result in JSON
		$sth = mysqli_query($conn, $query);
		$row = mysqli_fetch_assoc($sth);
		if ( $sth === false ) 
		{
		  printf("{\"result\":\"false\",\"error\":\"%s\"}", mysqli_error($conn));
		}
		else 
		{
		  if(password_verify($_POST["password"], $row["password"]))
			printf("{\"result\":\"true\"}");
		  else
			printf("{\"result\":\"false\",\"error\":\"Wrong username or password.\"}");
		}
	}
else
	{
		printf("{\"result\":\"false\",\"error\":\"Some required fields are missing!\"}");
	}
mysqli_close($conn);
?>