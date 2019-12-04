<?php

   if ($_SERVER['REQUEST_METHOD'] == 'POST')
	{
	
	require_once __DIR__ . '/connect.php';
    header('Content-type: application/json');	
		
	$obje=json_decode(file_get_contents('php://input'));
	
	$db=new Veritabani_Baglan();
	
	$con=$db->baglan();
	
	$dersid=$obje->dersid;
	
	$sorgu = $con->prepare("SELECT * FROM yoklama WHERE dersid=:dersid");
	$sorgu->bindParam(':dersid', $dersid, PDO::PARAM_STR);
    $sorgu->execute();
	
	$ogrenciidler=array();
	
	foreach($sorgu->fetchAll() as $row)
	{
	$ogrNo = array();
	$ogrNo["ogrNo"] = $row["ogrNo"];	
	array_push($ogrenciidler,$ogrNo);
	}
	$con = null;
	$db = null;
	if($sorgu)
	{	
	echo json_encode($ogrenciidler);
	return;
	}
    return;
	}

?>