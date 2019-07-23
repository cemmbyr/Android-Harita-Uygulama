package com.example.yazlab3;

import java.io.Serializable;

public class Firma implements Serializable {
    String firmaAdi;
    String firmaId;
    String firmaTuru;

    public String getFirmaAdi() {
        return firmaAdi;
    }

    public void setFirmaAdi(String firmaAdi) {
        this.firmaAdi = firmaAdi;
    }

    public String getFirmaId() {
        return firmaId;
    }

    public void setFirmaId(String firmaId) {
        this.firmaId = firmaId;
    }

    public String getFirmaTuru() {
        return firmaTuru;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLot() {
        return lot;
    }

    public void setLot(Double lot) {
        this.lot = lot;
    }

    public void setFirmaTuru(String firmaTuru) {
        this.firmaTuru = firmaTuru;
    }


    Double lat;
    Double lot;
}
