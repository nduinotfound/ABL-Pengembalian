package com.randu.pengembalian.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
// import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendHtmlEmailWithImage(String toEmail, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        
        // Parameter 'true' di sini menunjukkan ini adalah email multi-part (untuk attachment/inline)
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("auliarandu12@mail.com"); // Ganti dengan email pengirim Anda
        helper.setTo(toEmail);
        helper.setSubject(subject);
        
        // Mengatur konten sebagai HTML, parameter 'true' kedua
        helper.setText(htmlBody, true);

        // Menambahkan gambar sebagai sumber daya inline
        // CID (Content ID) 'bosRiperLogo' digunakan di PeminjamanMessageListener.java
        // ClassPathResource image = new ClassPathResource("static/logo.png"); 
        // helper.addInline("bosRiperLogo", image);

        mailSender.send(message);
        System.out.println("HTML Email with image sent successfully to: " + toEmail);
    }
}