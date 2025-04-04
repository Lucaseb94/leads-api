package com.advocacia.leads.infrastructure.controllers;

import com.advocacia.leads.domain.Role;
import com.advocacia.leads.domain.Usuario;
import com.advocacia.leads.dto.UserRegistrationRequest;
import com.advocacia.leads.infrastructure.repositories.RoleRepository;
import com.advocacia.leads.infrastructure.repositories.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

@RestController
@RequestMapping("/api/auth")
public class RegistrationController {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public RegistrationController(UsuarioRepository usuarioRepository,
                                  RoleRepository roleRepository,
                                  BCryptPasswordEncoder passwordEncoder) {
        System.out.println("🚀 RegistrationController carregado!");
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register-user")
    public ResponseEntity<String> register(@RequestBody UserRegistrationRequest request) {
        System.out.println("Requisição de cadastro recebida para: " + request.getEmail());

        // Verifica se já existe um usuário com esse email
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email já cadastrado");
        }

        // Cria um novo usuário e codifica a senha com BCrypt
        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));

        // Busca a role "ROLE_USER" no banco de dados e adiciona ao usuário
        Role roleUser = roleRepository.findByNome("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role não encontrada"));
        usuario.setRoles(new HashSet<>());
        usuario.getRoles().add(roleUser);

        usuarioRepository.save(usuario);
        return ResponseEntity.ok("Usuário cadastrado com sucesso");
    }
}
