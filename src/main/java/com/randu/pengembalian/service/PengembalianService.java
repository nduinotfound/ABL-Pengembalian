package com.randu.pengembalian.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.randu.pengembalian.model.Pengembalian;
import com.randu.pengembalian.repository.PengembalianRepository;
import com.randu.pengembalian.vo.Peminjaman;
import com.randu.pengembalian.vo.Buku;
import com.randu.pengembalian.vo.Anggota;
import com.randu.pengembalian.vo.ResponseTemplate;
import org.springframework.web.client.RestTemplate;

@Service
public class PengembalianService {

    @Autowired
    private PengembalianRepository pengembalianRepository;

    @Autowired
    private org.springframework.amqp.rabbit.core.RabbitTemplate rabbitTemplate;

    public List<Pengembalian> gettAllPengembalians(){
        return pengembalianRepository.findAll();
    }

    public Pengembalian getPengembalianById(Long id){
        return pengembalianRepository.findById(id).orElse(null);
    }

    public Pengembalian createPengembalian(Pengembalian pengembalian){
        Pengembalian savedPengembalian = pengembalianRepository.save(pengembalian);
        
        // Notify via RabbitMQ
        try {
            String exchange = "notification.exchange";
            String routingKey = "notification.key";
            String message = "Pengembalian Baru: Transaksi Peminjaman ID " + savedPengembalian.getPeminjamanId() + " telah dikembalikan.";
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
        } catch (Exception e) {
            System.err.println("Gagal mengirim notifikasi RabbitMQ: " + e.getMessage());
        }
        
        return savedPengembalian;
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

        Buku buku = null;
        Anggota anggota = null;
        if (peminjaman != null) {
            // Panggil service buku menggunakan bukuId yang ada di entitas peminjaman
            buku = restTemplate.getForObject(
                    "http://localhost:8001/api/buku/" + peminjaman.getBukuId(),
                    Buku.class);

            // Panggil service anggota menggunakan anggotaId yang ada di entitas peminjaman
            anggota = restTemplate.getForObject(
                    "http://localhost:8002/api/anggota/" + peminjaman.getAnggotaId(),
                    Anggota.class);
        }
        // Jika peminjaman tidak ditemukan, kembalikan VO dengan hanya pengembalian
        if (peminjaman == null) {
            ResponseTemplate vo = new ResponseTemplate();
            vo.setPengembalian(pengembalian);
            vo.setPeminjaman(null);
            responseList.add(vo);
            return responseList;
        }

        ResponseTemplate vo = new ResponseTemplate();
        vo.setPengembalian(pengembalian);
        vo.setPeminjaman(peminjaman);
        vo.setBuku(buku);
        vo.setAnggota(anggota);
        responseList.add(vo);
        return responseList;
    }
}
