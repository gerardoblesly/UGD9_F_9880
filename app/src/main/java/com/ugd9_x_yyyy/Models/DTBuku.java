package com.ugd9_x_yyyy.Models;

import java.io.Serializable;

public class DTBuku implements Serializable {
    private int idBuku, jumlah;
    private String noTransaksi, namaBuku, gambar;
    private Double harga;
    public Boolean isChecked = false;

    public DTBuku(int idBuku, String noTransaksi, int jumlah, String namaBuku,
                  Double harga, String gambar){
        this.idBuku         = idBuku;
        this.noTransaksi    = noTransaksi;
        this.jumlah         = jumlah;
        this.namaBuku       = namaBuku;
        this.harga          = harga;
        this.gambar         = gambar;
    }

    public int getIdBuku() {
        return idBuku;
    }

    public int getJumlah() {
        return jumlah;
    }

    public String getNoTransaksi() {
        return noTransaksi;
    }

    public String getNamaBuku() {
        return namaBuku;
    }

    public Double getHarga() {
        return harga;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getGambar() {
        return gambar;
    }
}
