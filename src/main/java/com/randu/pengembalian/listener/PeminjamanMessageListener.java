package com.randu.pengembalian.listener;

import com.randu.pengembalian.vo.Anggota;
import com.randu.pengembalian.vo.Buku;
import com.randu.pengembalian.vo.Peminjaman;
import com.randu.pengembalian.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PeminjamanMessageListener {

    private final RestTemplate restTemplate;
    private final EmailService emailService;

    public PeminjamanMessageListener(RestTemplate restTemplate, EmailService emailService) {
        this.restTemplate = restTemplate;
        this.emailService = emailService;
    }

    @RabbitListener(queues = "peminjaman_queue")
    public void onPeminjamanMessageReceived(Peminjaman peminjaman) throws MessagingException {
        System.out.println("Received Peminjaman from RabbitMQ: " + peminjaman);

        Buku buku = restTemplate.getForObject(
                "http://buku/api/buku/" + peminjaman.getBukuId(),
                Buku.class
        );
        Anggota anggota = restTemplate.getForObject(
                "http://anggota/api/anggota/" + peminjaman.getAnggotaId(),
                Anggota.class
        );

        if (anggota != null && buku != null && anggota.getEmail() != null) {
            
            String subject = "Pengembalian Buku Pustaka Dari Bos Riper";
            
            String htmlBody = "<html><body>"
                        + "<div style='border: 1px solid #ccc; padding: 20px; font-family: Arial, sans-serif;'>"
                        + "<h2><img src='cid:bosRiperLogo' style='width: 30px; height: 30px; margin-right: 10px;'/> Notifikasi Pengembalian Buku</h2>"
                        + "<p>Halo Saudara/i <b>" + anggota.getNama() + "</b>,</p>"
                        + "<p>Kami mengonfirmasi bahwa pengembalian buku Anda sedang diverifikasi. Berikut detail Anda dan peminjaman:</p>"
                        
                        + "<h3>Detail Anggota</h3>"
                        + "<ul style='list-style-type: none; padding-left: 0;'>"
                        + "<li>• <b>Nama:</b> " + anggota.getNama() + "</li>"
                        + "<li>• <b>NIM:</b> " + anggota.getNim() + "</li>"
                        + "<li>• <b>Jenis Kelamin:</b> " + anggota.getJenis_kelamin() + "</li>"
                        + "<li>• <b>Alamat:</b> " + anggota.getAlamat() + "</li>"
                        + "</ul>"
                        
                        + "<h3>Detail Peminjaman</h3>"
                        + "<ul style='list-style-type: none; padding-left: 0;'>"
                        + "<li>• <b>Judul Buku:</b> " + buku.getJudul() + "</li>"
                        + "<li>• <b>Pengarang:</b> " + buku.getPengarang() + "</li>"
                        + "<li>• <b>Tanggal Peminjaman:</b> " + peminjaman.getTanggal_pinjam() + "</li>"
                        + "<li>• <b>Tanggal Pengembalian:</b> " + peminjaman.getTanggal_kembali() + "</li>"
                        + "</ul>"
                        
                        + "<p>Tim Perpustakaan oleh Anggotanya Bos Riper akan menghubungi Anda untuk konfirmasi status pengembalian (Denda, Terlambat, dll.).</p>"
                        + "<p>Terima kasih. Salam Hangat dari Bos Riper:)</p>"
                        + "</div></body></html>";
            
            try {
                emailService.sendHtmlEmailWithImage(anggota.getEmail(), subject, htmlBody);
            } catch (jakarta.mail.MessagingException e) {
                System.err.println("Gagal mengirim email HTML: " + e.getMessage());
                throw new RuntimeException("Gagal mengirim email", e); 
            }
        } else {
            System.err.println("Gagal mengirim email: Data Anggota atau Buku tidak ditemukan atau Anggota tidak memiliki alamat email. ID Peminjaman: " + peminjaman.getId());
        }
    }
}