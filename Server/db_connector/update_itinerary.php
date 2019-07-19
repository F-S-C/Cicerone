<?php
/*
	Project: Cicerone
	Team: FSC
	File description: PHP connector that enables communication between Android 
	and MySQL. This file edit a row of the table 'itinerary'.
*/
include('configuration.php');

$query = "UPDATE itinerary SET "; //(attr = value, attr = value ...) WHERE username = $_POST['itinerary_code']
//TODO IMAGE_URL INTO QUERY

$data = Array();

if(isset($_POST['username'])){
    
    if(isset($_POST['title']))
    {
        array_push($data, "title = '" . $_POST['title'] . "'");
    }
    if(isset($_POST['description']))
    {
        array_push($data, "description = '" . $_POST['description'] . "'");
    }
    if(isset($_POST['beginning_date']))
    {
        array_push($data, "beginning_date = '" . $_POST['beginning_date'] . "'");
    }
    if(isset($_POST['ending_date']))
    {
        array_push($data, "ending_date = '" . $_POST['ending_date'] . "'");
    }
    if(isset($_POST['end_reservations_date']))
    {
        array_push($data, "end_reservations_date = '" . $_POST['end_reservations_date'] . "'");
    }
    if(isset($_POST['maximum_participants_number']))
    {
        array_push($data, "maximum_participants_number = '" . $_POST['maximum_participants_number'] . "'");
    }
    if(isset($_POST['minimum_participants_number']))
    {
        array_push($data, "minimum_participants_number = '" . $_POST['minimum_participants_number'] . "'");
    }
    if(isset($_POST['location']))
    {
        array_push($data, "location = '" . $_POST['location'] . "'");
    }
    if(isset($_POST['repetitions_per_day']))
    {
        array_push($data, "repetitions_per_day = '" . $_POST['repetitions_per_day'] . "'");
    }
	if(isset($_POST['duration']))
    {
        array_push($data, "duration = '" . $_POST['duration'] . "'");
    }
    if(isset($_POST['full_price']))
    {
        array_push($data, "full_price = '" . $_POST['full_price'] . "'");
    }
	 if(isset($_POST['reduced_price']))
    {
        array_push($data, "reduced_price = '" . $_POST['reduced_price'] . "'");
    }
	
    
    addValuesToQuery($data,$query);
    $query .= " WHERE itinerary_code = '" . $_POST['itinerary_code'] . "'";
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


mysqli_close($conn);
?>