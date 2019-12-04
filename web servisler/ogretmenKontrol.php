<?php

	if ($_SERVER['REQUEST_METHOD'] == 'POST')
	{
	require_once __DIR__ . '/connect.php';
    header('Content-type: application/json');	
	
	$obje=json_decode(file_get_contents('php://input'));
	
	$db=new Veritabani_Baglan();
	
	$con=$db->baglan();
	
	$kullaniciadi=$obje->kullaniciadi;
	$sifre=$obje->sifre;
	
	$sorgu = $con->prepare("SELECT * FROM ogretmenler WHERE kullaniciadi=:kullaniciadi AND sifre =:sifre");
	$sorgu->bindParam(':kullaniciadi', $kullaniciadi, PDO::PARAM_STR);
    $sorgu->bindParam(':sifre', $sifre, PDO::PARAM_STR);
    $sorgu->execute();


	$sonuc = $sorgu->fetch(PDO::FETCH_ASSOC);
	$ogretmenid=array();
	$ogretmenid["ogretmenid"] = $sonuc["ogretmenid"];
	echo json_encode($ogretmenid);
	
	}
	
	
	
?>