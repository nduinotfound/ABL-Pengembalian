package com.randu.pengembalian.vo;

public class Anggota {
    private Long id;
    private int nim;
    private String nama;
    private String alamat;
    private String jenis_kelamin;
    private String email;

    public Anggota() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNim() {
        return nim;
    }

    public void setNim(int nim) {
        this.nim = nim;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public void setJenis_kelamin(String jenis_kelamin) {
        this.jenis_kelamin = jenis_kelamin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public String toString() {
        return "Anggota [id=" + id + ", nim=" + nim + ", nama=" + nama + ", alamat=" + alamat + ", jenis_kelamin="
                + jenis_kelamin + ", email=" + email + "]";
    }
    
}