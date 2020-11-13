package com.ugd9_x_yyyy.Models;

import java.io.Serializable;

public class Buku implements Serializable {
    private int idBuku;
    private String namaBuku, pengarang, gambar;
    private Double harga;

    public Buku (int idBuku, String namaBuku, String pengarang, Double harga, String gambar) {
        this.idBuku = idBuku;
        this.namaBuku = namaBuku;
        this.pengarang = pengarang;
        this.harga = harga;
        this.gambar = gambar;
    }

    public Buku (String namaBuku, String pengarang, Double harga) {
        this.namaBuku = namaBuku;
        this.pengarang = pengarang;
        this.harga = harga;
    }

    public Double getHarga() {
        return harga;
    }

    public int getIdBuku() {
        return idBuku;
    }

    public String getNamaBuku() {
        return namaBuku;
    }

    public String getPengarang() {
        return pengarang;
    }

    public String getGambar() {
        return gambar;
    }
}
