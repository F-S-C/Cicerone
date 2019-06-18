<?php
/*
	Project: Cicerone
	Team: FSC
	File description: PHP connector that enables communication between Android 
	and MySQL. This file insert data into table 'registered_user'.
*/
include('configuration.php');

$query = "INSERT INTO registered_user (username, tax_code, name, surname, password, email, user_type, cellphone, birth_date, sex) VALUES (";

// Checks if the variables passed via POST exist and adds them to the query
if(isset($_POST['username']) && isset($_POST['tax_code']) && isset($_POST['name']) && isset($_POST['surname']) && isset($_POST['password']) && isset($_POST['email']) && isset($_POST['user_type']) && isset($_POST['cellphone']) && isset($_POST['birth_date']) && isset($_POST['sex']))
	{
		$query .= strtolower("'" . $_POST['username'] . "','" . $_POST['tax_code'] . "','" . $_POST['name'] . "','" . $_POST['surname']) . "','" . password_hash($_POST['password'],PASSWORD_DEFAULT) . "','" . strtolower($_POST['email'] . "','" . $_POST['user_type'] . "','" . $_POST['cellphone'] . "','" . $_POST['birth_date']. "','" . $_POST['sex']). "')";

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