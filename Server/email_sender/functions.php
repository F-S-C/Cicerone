<?php
ini_set('display_errors', 1);
function getMailPage($filename, $variablesToMakeLocal) {
    extract($variablesToMakeLocal);
    if (is_file($filename)) {
        ob_start();
        include $filename;
        return ob_get_clean();
    }
    return false;
}

function getDataFromDB($username, $itinerary_code){
    $link = mysqli_connect("localhost", "fsc", "89n@W[", "cicerone");
    if($link === false){
        echo '{"result":false,"error":"Could not connect.  ' . mysqli_connect_error() . '"}';
        exit;
    }
    //USER DATA
    $sql = "SELECT * FROM registered_user WHERE username = '" . $username . "'";
    if($result = mysqli_query($link, $sql)){
        if(mysqli_num_rows($result) > 0){
            $row = mysqli_fetch_array($result);
            $user = array('name' => $row['name'],'surname' => $row['surname'],'email' => $row['email'],'cellphone' => $row['cellphone']);
            mysqli_free_result($result);
        }else{
            echo '{"result":false,"error":"Username not found."}';
            exit;
        }
    } else{
        echo "ERROR: Could not able to execute $sql. " . mysqli_error($link);
    }
    
    $sql = "SELECT * FROM itinerary WHERE itinerary_code = '" . $itinerary_code . "'";
    if($result = mysqli_query($link, $sql)){
        if(mysqli_num_rows($result) > 0){
            $row = mysqli_fetch_array($result);
            $itinerary = array('title' => $row['title'],'location' => $row['location'],'beginning_date' => $row['beginning_date'],'ending_date' => $row['ending_date']);
            mysqli_free_result($result);
        }else{
            echo '{"result":false,"error":"Itinerary not found."}';
            exit;
        }
    } else{
        echo '{"result":false,"error":"Could not able to execute $sql. ' . mysqli_error($link) . '"}';
        exit;
    }
    
    mysqli_close($link);
    return array_merge($user, $itinerary);
}

function getSMTPData($id){
    switch($id){
        case 1:
            return 'noreply.cicerone.app@gmail.com';
        case 2:
            return 'X36Y$^8s6u';
        case 3:
            return 'Cicerone';
        default:
            return null;
    }
}
?>