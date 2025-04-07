package com.advocacia.leads.infrastructure.services;

import com.advocacia.leads.domain.ResetPasswordToken;
import com.advocacia.leads.domain.Usuario;
import com.advocacia.leads.infrastructure.repositories.ResetPasswordTokenRepository;
import com.advocacia.leads.infrastructure.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResetPasswordService {

    @Autowired
    private UsuarioRepository userRepository;

    @Autowired
    private ResetPasswordTokenRepository tokenRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void sendResetToken(String email) {
        Optional<Usuario> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) return; // Segurança: não revela se o email existe

        Usuario user = optionalUser.get();

        // Verifica se o email está presente no cadastro
        if (user.getEmail() == null || user.getEmail().isBlank()) return;

        String token = UUID.randomUUID().toString();

        ResetPasswordToken resetToken = ResetPasswordToken.builder()
                .token(token)
                .expirationDate(LocalDateTime.now().plusHours(1))
                .used(false)
                .usuario(user)
                .email(user.getEmail())
                .build();

        tokenRepository.save(resetToken);

        String message = "Use o token abaixo para redefinir sua senha via API:\n\n"
                + "Token: " + token + "\n"
                + "Endpoint: POST /api/auth/reset-password\n\n"
                + "Envie o seguinte JSON:\n"
                + "{\n  \"token\": \"" + token + "\",\n  \"novaSenha\": \"SUA_NOVA_SENHA\"\n}";

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setSubject("Redefinição de senha");
        mail.setText(message);

        mailSender.send(mail);
    }



    public boolean resetPassword(String token, String novaSenha) {
        Optional<ResetPasswordToken> optionalToken = tokenRepository.findByToken(token);

        if (optionalToken.isEmpty()) return false;

        ResetPasswordToken resetToken = optionalToken.get();

        // Verifica se o token expirou ou já foi usado
        if (resetToken.getExpirationDate().isBefore(LocalDateTime.now()) || resetToken.isUsed()) {
            return false;
        }

        Usuario user = resetToken.getUsuario();

        // Atualiza a senha codificada
        user.setSenha(passwordEncoder.encode(novaSenha));
        userRepository.save(user);

        // Marca o token como usado e salva
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);

        return true;
    }
}
