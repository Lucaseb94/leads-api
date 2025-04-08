package com.advocacia.leads.infrastructure.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviar(String para, String assunto, String corpo) {
        System.out.println("Enviando e-mail para: " + para);
        System.out.println("Assunto: " + assunto);
        System.out.println("Corpo do e-mail:\n" + corpo);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(para);
        message.setSubject(assunto);
        message.setText(corpo);

        try {
            mailSender.send(message);
            System.out.println("E-mail enviado com sucesso para: " + para);
        } catch (Exception e) {
            System.err.println("Erro ao enviar e-mail para: " + para);
            e.printStackTrace();
        }
    }
}
