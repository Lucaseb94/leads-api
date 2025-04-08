package com.advocacia.leads.infrastructure.controllers;

import com.advocacia.leads.dto.JwtResponse;
import com.advocacia.leads.dto.LoginRequest;
import com.advocacia.leads.dto.UserRegistrationRequest;
import com.advocacia.leads.infrastructure.services.ResetPasswordService;
import com.advocacia.leads.security.JwtUtil;
import com.advocacia.leads.infrastructure.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final ResetPasswordService resetPasswordService;

    // Injeção via construtor (melhor prática)
    public AuthController(
            AuthenticationManager authManager,
            JwtUtil jwtUtil,
            UserService userService,
            ResetPasswordService resetPasswordService
    ) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.resetPasswordService = resetPasswordService;
    }

    // Login do usuário
    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        String jwt = jwtUtil.generateToken(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    // Registro de novo usuário
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegistrationRequest request) {
        try {
            userService.registerUser(request.getEmail(), request.getSenha(), request.getNome(),request.getEspecializacao());
            return ResponseEntity.ok("Usuário registrado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao registrar usuário: " + e.getMessage());
        }
    }

    // Envio do token de redefinição de senha
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        resetPasswordService.sendResetToken(email);
        return ResponseEntity.ok("Se o e-mail estiver cadastrado, um token foi enviado.");
    }

    // Redefinição da senha com token
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String novaSenha = request.get("novaSenha");

        boolean success = resetPasswordService.resetPassword(token, novaSenha);

        if (success) {
            return ResponseEntity.ok("Senha redefinida com sucesso!");
        } else {
            return ResponseEntity.badRequest().body("Token inválido ou expirado.");
        }
    }
}
