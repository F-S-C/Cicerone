<?php
/*
	Project: Cicerone
	Team: FSC
	File description: PHP connector that enables communication between Android 
	and MySQL. This file request data from table 'report_detail'.
*/
include('configuration.php');

$query = "SELECT * FROM report_detail";

// Checks if the variables passed via POST exist and adds them to the query
if(isset($_POST['report_code']))
{
	$query .= " WHERE report_code = '" . $_POST['report_code'] . "'";
}

// Send the query to MySQL and print received records in JSON
requestData($conn, $query);
?>