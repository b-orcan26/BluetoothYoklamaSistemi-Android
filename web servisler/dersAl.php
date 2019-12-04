<?php

	if ($_SERVER['REQUEST_METHOD'] == 'POST')
	{
	
	require_once __DIR__ . '/connect.php';
    header('Content-type: application/json');	
	
	//$obje=json_decode(file_get_contents('php://input'),true);
	$obje=json_decode(file_get_contents('php://input'));
	
	$db=new Veritabani_Baglan();
	
	$con=$db->baglan();
	
	//$ogretmenid=$obje[0]['ogretmenid'];
	//$ogretimturu=$obje[0]['ogretimturu'];
	
	  $ogretmenid=$obje->ogretmenid;
	  $ogretimturu=$obje->ogretimturu;
	
	$sorgu = $con->prepare("SELECT * FROM verir WHERE ogretmenid=:ogretmenid");
	$sorgu->bindParam(':ogretmenid', $ogretmenid, PDO::PARAM_STR);
    $sorgu->execute();

	$count=$sorgu->rowCount();
	
	
	$dersid=array();
	$i=0;
	foreach($sorgu->fetchAll() as $row)
	{
	 $dersid[$i]=$row["dersid"];
	 $i++;
	}

	
	
	
	
	$count=count($dersid);
	$dersler=array();
	
	
	for($j = 0 ; $j< $count ; $j++)
	{
		
		$sorgu1=$con->prepare("SELECT * FROM dersler WHERE dersid=:dersid AND ogretimturu=:ogretimturu");
		$sorgu1->bindParam('dersid',$dersid[$j],PDO::PARAM_STR);
		$sorgu1->bindParam('ogretimturu',$ogretimturu,PDO::PARAM_STR);
		$sorgu1->execute();
		
		$count1=$sorgu1->rowCount();
		
		if($count1>0)
		{
			$sonuc=$sorgu1->fetch(PDO::FETCH_ASSOC);
			$newders=array();
			$newders["dersid"]=$sonuc["dersid"];
			$newders["dersadi"]=$sonuc["dersadi"];
			array_push($dersler,$newders);
		
		
		}
		else
		{
		}
		
		
	}
	
	echo json_encode($dersler);
	}
	
	
	
?>