package com.advocacia.leads.infrastructure.services;

import com.advocacia.leads.domain.Role;
import com.advocacia.leads.domain.Usuario;
import com.advocacia.leads.dto.RoleChangeDTO;
import com.advocacia.leads.dto.UsuarioDTO;
import com.advocacia.leads.infrastructure.repositories.RoleRepository;
import com.advocacia.leads.infrastructure.repositories.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UsuarioRepository usuarioRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerUser(String email, String senha, String especializacao, String requestEspecializacao) {
        // Verifica se o e-mail já está cadastrado
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("E-mail já está em uso.");
        }

        Usuario novoUsuario = Usuario.builder()
                .email(email)
                .senha(passwordEncoder.encode(senha))
                .especializacao(especializacao)
                .ativo(true)
                .build();

        usuarioRepository.save(novoUsuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(u -> new UsuarioDTO(u.getNome(), u.getEmail(), u.getEspecializacao()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void atualizar(Long id, UsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuario.setNome(dto.nome());
        usuario.setEspecializacao(dto.especializacao());
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void desativarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void alterarRoles(RoleChangeDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Role role = roleRepository.findByNome("ROLE_" + dto.role().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Role não encontrada"));

        if (dto.adicionar()) {
            usuario.getRoles().add(role);
        } else {
            usuario.getRoles().remove(role);
        }
        usuarioRepository.save(usuario);
    }


    @Transactional
    public void reativarUsuario(Long id) {
        var usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuario.setAtivo(true);
        usuarioRepository.save(usuario);
    }

}
