package com.randu.pengembalian.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.randu.pengembalian.model.Pengembalian;
import com.randu.pengembalian.repository.PengembalianRepository;
import com.randu.pengembalian.vo.Peminjaman;
import com.randu.pengembalian.vo.ResponseTemplate;
import org.springframework.web.client.RestTemplate;

@Service
public class PengembalianService {

    @Autowired
    private PengembalianRepository pengembalianRepository;

    public List<Pengembalian> gettAllPengembalians(){
        return pengembalianRepository.findAll();
    }

    public Pengembalian getPengembalianById(Long id){
        return pengembalianRepository.findById(id).orElse(null);
    }

    public Pengembalian createPengembalian(Pengembalian pengembalian){
        return pengembalianRepository.save(pengembalian);
    }

    public void deletePengembalian(Long id){
        pengembalianRepository.deleteById(id);
    }

    public List<ResponseTemplate> getPengembalianWithPeminjamanById(Long id){
        List<ResponseTemplate> responseList = new ArrayList<>();
        Pengembalian pengembalian = getPengembalianById(id);
        // jika pengembalian tidak ditemukan, kembalikan list kosong
        if (pengembalian == null) {
            return responseList;
        }

        // Inisialisasi RestTemplate lokal (atau gunakan @Autowired jika ada bean)
        RestTemplate restTemplate = new RestTemplate();

        // Panggil service peminjaman menggunakan peminjamanId yang ada di entitas pengembalian
        Peminjaman peminjaman = restTemplate.getForObject(
                "http://localhost:8003/api/peminjaman/" + pengembalian.getPeminjamanId(),
                Peminjaman.class);
        // Jika peminjaman tidak ditemukan, kembalikan VO dengan hanya pengembalian
        if (peminjaman == null) {
            ResponseTemplate vo = new ResponseTemplate(pengembalian, null);
            responseList.add(vo);
            return responseList;
        }

        ResponseTemplate vo = new ResponseTemplate(pengembalian, peminjaman);
        responseList.add(vo);
        return responseList;
    }
}
