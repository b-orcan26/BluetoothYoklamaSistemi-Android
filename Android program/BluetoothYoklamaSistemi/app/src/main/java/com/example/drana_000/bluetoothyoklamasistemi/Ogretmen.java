package com.example.drana_000.bluetoothyoklamasistemi;

/**
 * Created by Drana_000 on 17.5.2017.
 */

import org.json.JSONArray;


public class Ogretmen {
    private static Ogretmen ourInstance = new Ogretmen();
    public static String ogretmenid;
    public static String ogretimTürü;
    public static String dersid;
    public static JSONArray dersler;
    public static String kullaniciadi;
    public static String sifre;

    public static Ogretmen getInstance() {
        return ourInstance;
    }

    private Ogretmen() {
        ogretmenid = "null";
        ogretimTürü = "null";
        dersid = "null";
        kullaniciadi="null";
        sifre="null";
        dersler = new JSONArray();
    }

    public String getKullaniciadi()
    {
        return kullaniciadi;
    }

    public void setKullaniciadi(String kullaniciadi)
    {
        this.kullaniciadi=kullaniciadi;
    }

    public String getSifre()
    {
        return sifre;
    }

    public void setSifre(String sifre)
    {
        this.sifre=sifre;
    }

    public String getOgretmenID() {
        return ogretmenid;
    }

    public void setOgretmenid(String id) {
        ogretmenid = id;
    }

    public String getOgretimTürü() {
        return ogretimTürü;
    }

    public void setOgretimTürü(String tür) {
        ogretimTürü = tür;
    }

    public void setDersID(String id) {
        dersid = id;
    }

    public String getDersID() {
        return dersid;
    }

    public JSONArray getDersler() {
        return dersler;
    }

    public void setDersler(JSONArray jsonArray) {
        dersler = jsonArray;
    }

}

