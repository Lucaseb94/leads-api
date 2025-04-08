package com.advocacia.leads.infrastructure.repositories;

import com.advocacia.leads.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    long countByAtivoTrue();
    long countByAtivoFalse();
    Optional<Usuario> findByEmail(String email);
}
