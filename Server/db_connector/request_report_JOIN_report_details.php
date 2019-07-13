<?php
/*
	Project: Cicerone
	Team: FSC
	File description: PHP connector that enables communication between Android 
	and MySQL. This file request data from table 'report'.
*/
include('configuration.php');

$query = "SELECT * FROM report INNER JOIN report_details ON report.report_code = report_details.report_code";
$condition = Array();

// Checks if the variables passed via POST exist and adds them to the query
if(isset($_POST['username']))
{
	array_push($condition, "username = '" . strtolower($_POST['username']) . "'");
}
if(isset($_POST['reported_user']))
{
	array_push($condition, "reported_user = '" . strtolower($_POST['reported_user']) . "'");
}
if(isset($_POST['report_code']))
{
	array_push($condition, "report.report_code = '" . $_POST['report_code'] . "'");
}

addConditionsToQuery($condition, $query);

// Send the query to MySQL and print received records in JSON
requestData($conn, $query);
?>