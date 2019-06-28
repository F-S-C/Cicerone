<?php
/*
	Project: Cicerone
	Team: FSC
	File description: PHP connector that enables communication between Android 
	and MySQL. This file updates the data in the 'registered_user' table.
*/
include('configuration.php');

$query = "UPDATE registered_user SET "; //(attr = value, attr = value ...) WHERE username = $_POST['username']
$data = Array();

if(isset($_POST['username'])){
    
    if(isset($_POST['tax_code']))
    {
        array_push($data, "tax_code = '" . $_POST['tax_code'] . "'");
    }
    if(isset($_POST['name']))
    {
        array_push($data, "name = '" . $_POST['name'] . "'");
    }
    if(isset($_POST['surname']))
    {
        array_push($data, "surname = '" . $_POST['surname'] . "'");
    }
    if(isset($_POST['password']))
    {
        array_push($data, "password = '" . password_hash($_POST['password'],PASSWORD_DEFAULT) . "'");
    }
    if(isset($_POST['email']))
    {
        array_push($data, "email = '" . $_POST['email'] . "'");
    }
    if(isset($_POST['user_type']))
    {
        array_push($data, "user_type = '" . $_POST['user_type'] . "'");
    }
    if(isset($_POST['cellphone']))
    {
        array_push($data, "cellphone = '" . $_POST['cellphone'] . "'");
    }
    if(isset($_POST['birthday']))
    {
        array_push($data, "birthday = '" . $_POST['birthday'] . "'");
    }
    if(isset($_POST['sex']))
    {
        array_push($data, "sex = '" . $_POST['sex'] . "'");
    }
    
    addValuesToQuery($data,$query);
    $query .= " WHERE username = '" . $_POST['username'] . "'";
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