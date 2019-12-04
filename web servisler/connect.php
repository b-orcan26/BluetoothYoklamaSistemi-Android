<?php

	class Veritabani_Baglan {

		function baglan() 
		{
		require_once __DIR__ . '/config.php';
		try 
	    {
			$con = new PDO("mysql:host=".DB_HOST.";dbname=".DB_DATABASE.";charset=utf8", DB_USER, DB_PASSWORD);
		} 
		catch ( PDOException $e ){
			print $e->getMessage();
		}
        return $con;
    }
    function close() 
	{  
		$con = null; 
	}
}




?>