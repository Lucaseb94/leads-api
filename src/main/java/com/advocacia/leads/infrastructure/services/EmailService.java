package com.advocacia.leads.infrastructure.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviar(String para, String assunto, String corpoHtml) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            // 'true' indica que a mensagem é multipart (permitindo HTML)
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(para);
            helper.setSubject(assunto);
            helper.setText(corpoHtml, true); // O segundo parâmetro 'true' especifica que o conteúdo é HTML

            mailSender.send(message);

            System.out.println("Email HTML enviado com sucesso para: " + para);
        } catch (MessagingException e) {
            System.err.println("Erro ao enviar email HTML: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
