package com.randu.pengembalian.vo;

import java.sql.Date;

public class Peminjaman {

    private Long id;
    private Long anggotaId;
    private Long bukuId;
    private Date tanggal_pinjam;
    private Date tanggal_kembali;
    public Peminjaman() {
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getAnggotaId() {
        return anggotaId;
    }
    public void setAnggotaId(Long anggotaId) {
        this.anggotaId = anggotaId;
    }
    public Long getBukuId() {
        return bukuId;
    }
    public void setBukuId(Long bukuId) {
        this.bukuId = bukuId;
    }
    public Date getTanggal_pinjam() {
        return tanggal_pinjam;
    }
    public void setTanggal_pinjam(Date tanggal_pinjam) {
        this.tanggal_pinjam = tanggal_pinjam;
    }
    public Date getTanggal_kembali() {
        return tanggal_kembali;
    }
    public void setTanggal_kembali(Date tanggal_kembali) {
        this.tanggal_kembali = tanggal_kembali;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }
    
}