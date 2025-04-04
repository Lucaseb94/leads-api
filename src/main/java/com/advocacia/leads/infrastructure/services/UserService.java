package com.advocacia.leads.infrastructure.services;

import com.advocacia.leads.domain.AreaDireito;
import com.advocacia.leads.domain.Usuario;
import com.advocacia.leads.infrastructure.repositories.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.advocacia.leads.infrastructure.repositories.RoleRepository;
@Service
public class UserService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;  // <-- Adicionado
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UsuarioRepository usuarioRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerUser(String email, String senha, String especializacaoStr) {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setEmail(email);
        novoUsuario.setSenha(passwordEncoder.encode(senha)); // Criptografa a senha

        try {
            AreaDireito especializacao = AreaDireito.valueOf(especializacaoStr.toUpperCase()); // Converte String para Enum
            novoUsuario.setEspecializacao(especializacao.name());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Especialização inválida: " + especializacaoStr);
        }

        usuarioRepository.save(novoUsuario);
    }
}