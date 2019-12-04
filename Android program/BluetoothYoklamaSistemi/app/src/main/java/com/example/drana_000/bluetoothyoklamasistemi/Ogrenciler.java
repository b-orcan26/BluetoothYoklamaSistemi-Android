package com.example.drana_000.bluetoothyoklamasistemi;

/**
 * Created by Drana_000 on 17.5.2017.
 */

public class Ogrenciler {

    private String ogrenciNo;
     boolean derseKatilim;

    public Ogrenciler(String ogrenciNo) {
        this.ogrenciNo = ogrenciNo;
        this.derseKatilim = false;
    }

    public String getOgrenciNo() {
        return ogrenciNo;
    }

    public void setOgrenciNo(String ogrenciNo) {
        this.ogrenciNo = ogrenciNo;
    }

    public boolean getDerseKatilim() {
        return derseKatilim;
    }

    public void setDerseKatilim(Boolean derseKatilim) {
        this.derseKatilim = derseKatilim;
    }

    public boolean equals(Ogrenciler ogrenci) {
        if (ogrenci.getOgrenciNo().equals(this.getOgrenciNo())) {
            return true;
        }
        return false;
    }
}
