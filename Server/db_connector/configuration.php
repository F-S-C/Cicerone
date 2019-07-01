<?php
/*
	Project: Cicerone
	Team: FSC
	File description: This file contains the database access credentials.
*/
$servername = "127.0.0.1";
$username = "fsc";
$p = "89n@W[";
$db = "cicerone";

// Create connection
$conn = new mysqli($servername, $username, $p, $db);

// Check connection
if ($conn->connect_error) {
	die("Connection failed: " . $conn->connect_error);
}

function requestData($conn, $query)
{
	$sth = mysqli_query($conn, $query);
	$rows = array();
	if ($sth === false) {
		printf("{\"result\":\"false\",\"error\":\"%s\"}", mysqli_error($conn));
	} else {
		while ($r = mysqli_fetch_assoc($sth)) {
			$rows[] = $r;
		}
		print json_encode($rows);
	}
	mysqli_close($conn);
}

function addConditionsToQuery($condition, &$query)
{
	if (count($condition) > 0) {
		$query = $query . " WHERE ";
		while (count($condition) > 0) {
			$query = $query . $condition[0] ?? '';
			array_shift($condition);
			if (count($condition) > 0) {
				$query = $query . " AND ";
			}
		}
	}
}

function addValuesToQuery($values, &$update)
{
	if (count($values) > 0) {
		while (count($values) > 0) {
			$update .= $values[0] ?? '';
			array_shift($values);
			if (count($values) > 0) {
				$update .= " , ";
			}
		}
	}
}
