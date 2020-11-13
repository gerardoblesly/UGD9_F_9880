package com.ugd9_x_yyyy.Models;

import java.io.Serializable;
import java.util.List;

public class TransaksiBuku implements Serializable {
    private String noTransaksi, tglTransaksi, namaToko;
    private Integer idToko;
    private Double totalBiaya;
    private List<DTBuku> dtBukuList;
    public Boolean isChecked = false;

    public TransaksiBuku (String noTransaksi, int idToko, String tglTransaksi,
                          Double totalBiaya, String namaToko, List<DTBuku> dtBukuList){
        this.noTransaksi    = noTransaksi;
        this.idToko            = idToko;
        this.tglTransaksi   = tglTransaksi;
        this.totalBiaya     = totalBiaya;
        this.namaToko = namaToko;
        this.dtBukuList     = dtBukuList;
    }

    public TransaksiBuku (int idToko, Double totalBiaya){
        this.idToko = idToko;
        this.totalBiaya = totalBiaya;
    }

    public String getNamaToko() {
        return namaToko;
    }

    public String getNoTransaksi() {
        return noTransaksi;
    }

    public Double getTotalBiaya() {
        return totalBiaya;
    }

    public int getIdToko() {
        return idToko;
    }

    public String getTglTransaksi() {
        return tglTransaksi;
    }

    public List<DTBuku> getDtBukuList() {
        return dtBukuList;
    }

    public void setDtBukuList(List<DTBuku> dtBukuList) {
        this.dtBukuList = dtBukuList;
    }

    public void setTotalBiaya(Double totalBiaya) {
        this.totalBiaya = totalBiaya;
    }
}
