package com.ugd9_x_yyyy.Models;

import java.io.Serializable;

public class Mahasiswa implements Serializable {

    private String npm, nama, jenis_kelamin, prodi, gambar;

    public Mahasiswa(String npm, String nama, String jenis_kelamin, String prodi, String gambar){
        this.npm = npm;
        this.nama = nama;
        this.jenis_kelamin = jenis_kelamin;
        this.prodi = prodi;
        this.gambar = gambar;
    }

    public String getNpm() {
        return npm;
    }

    public String getNama() {
        return nama;
    }

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public String getProdi() {
        return prodi;
    }

    public String getGambar() {
        return gambar;
    }
}

