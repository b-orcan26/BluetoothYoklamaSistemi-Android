<?php

	if ($_SERVER['REQUEST_METHOD'] == 'POST')
	{
	require_once __DIR__ . '/connect.php';
    header('Content-type: application/json');	
    
	
	$jsonArray=json_decode(file_get_contents('php://input'));
	$count=count($jsonArray);
	
	///////////////////////
	$kullaniciadi=$jsonArray[1]->{"kullaniciadi"};
	$sifre=$jsonArray[1]->{"sifre"};
	
	//dersid elde edildi
	$dersid=$jsonArray[0]->{"dersid"};
	
	$db=new Veritabani_Baglan();
	$con=$db->baglan();
	
	$sorgu1 = $con->prepare("SELECT * FROM ogretmenler WHERE kullaniciadi=:kullaniciadi AND sifre=:sifre");
	$sorgu1->bindParam(':kullaniciadi', $kullaniciadi, PDO::PARAM_STR);
    $sorgu1->bindParam(':sifre', $sifre, PDO::PARAM_STR);
	
    $select=$sorgu1->execute();

	if ( $select )
	{    
	
	$query = $con->query("SELECT hafta1,hafta2,hafta3,hafta4,hafta5 FROM yoklama WHERE dersid={$dersid}")->fetch(PDO::FETCH_ASSOC);
	
	if ($query['hafta1']==null)
	{
		//hafta 1 e ekle;
		for($i=1 ; $i<$count ; $i++)
		{
			$ogrNo=$jsonArray[$i]->{"ogrNo"};
			$katilim=$jsonArray[$i]->{"katilim"};
			
			$sorgu = $con->prepare("UPDATE yoklama SET hafta1=:katilim WHERE dersid=:dersid AND ogrNo=:ogrNo");
			$sorgu->bindParam(':katilim', $katilim, PDO::PARAM_STR);
			$sorgu->bindParam(':dersid',$dersid,PDO::PARAM_INT);
			$sorgu->bindParam(':ogrNo',$ogrNo,PDO::PARAM_INT);
			$update=$sorgu->execute();
		}
		echo "yoklama kaydedildi";
	}
	
	else if($query['hafta2']==null)
	{
		for($i=1 ; $i<$count ; $i++)
		{
			$ogrNo=$jsonArray[$i]->{"ogrNo"};
			$katilim=$jsonArray[$i]->{"katilim"};
			
			$sorgu = $con->prepare("UPDATE yoklama SET hafta2=:katilim WHERE dersid=:dersid AND ogrNo=:ogrNo");
			$sorgu->bindParam(':katilim', $katilim, PDO::PARAM_STR);
			$sorgu->bindParam(':dersid',$dersid,PDO::PARAM_INT);
			$sorgu->bindParam(':ogrNo',$ogrNo,PDO::PARAM_INT);
			$update=$sorgu->execute();
		}
		echo "yoklama kaydedildi";
	}
	
	else if($query['hafta3']==null)
	{
		for($i=1 ; $i<$count ; $i++)
		{
			$ogrNo=$jsonArray[$i]->{"ogrNo"};
			$katilim=$jsonArray[$i]->{"katilim"};
			
			$sorgu = $con->prepare("UPDATE yoklama SET hafta3=:katilim WHERE dersid=:dersid AND ogrNo=:ogrNo");
			$sorgu->bindParam(':katilim', $katilim, PDO::PARAM_STR);
			$sorgu->bindParam(':dersid',$dersid,PDO::PARAM_INT);
			$sorgu->bindParam(':ogrNo',$ogrNo,PDO::PARAM_INT);
			$update=$sorgu->execute();
		}
		echo "yoklama kaydedildi";
	}
	else if($query['hafta4']==null)
	{
		for($i=1 ; $i<$count ; $i++)
		{
			$ogrNo=$jsonArray[$i]->{"ogrNo"};
			$katilim=$jsonArray[$i]->{"katilim"};
			
			$sorgu = $con->prepare("UPDATE yoklama SET hafta4=:katilim WHERE dersid=:dersid AND ogrNo=:ogrNo");
			$sorgu->bindParam(':katilim', $katilim, PDO::PARAM_STR);
			$sorgu->bindParam(':dersid',$dersid,PDO::PARAM_INT);
			$sorgu->bindParam(':ogrNo',$ogrNo,PDO::PARAM_INT);
			$update=$sorgu->execute();
		}
		echo "yoklama kaydedildi";
	}
	
	else if($query['hafta5']==null)
	{
		for($i=1 ; $i<$count ; $i++)
		{
			$ogrNo=$jsonArray[$i]->{"ogrNo"};
			$katilim=$jsonArray[$i]->{"katilim"};
			
			$sorgu = $con->prepare("UPDATE yoklama SET hafta5=:katilim WHERE dersid=:dersid AND ogrNo=:ogrNo");
			$sorgu->bindParam(':katilim', $katilim, PDO::PARAM_STR);
			$sorgu->bindParam(':dersid',$dersid,PDO::PARAM_INT);
			$sorgu->bindParam(':ogrNo',$ogrNo,PDO::PARAM_INT);
			$update=$sorgu->execute();
		}
		echo "yoklama kaydedildi";
	}
	}
	
	else
	{
		echo "Yetkiniz yok.";
	}
	}
?>