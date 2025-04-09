package com.advocacia.leads.infrastructure.repositories;

import com.advocacia.leads.domain.AreaDireito;
import com.advocacia.leads.domain.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Já usado pelo controller LeadController
    @Query("SELECT u FROM Usuario u WHERE u.ativo = true AND LOWER(u.especializacao) = LOWER(:especializacao) AND LOWER(u.regiao) = LOWER(:regiao)")
    List<Usuario> buscarPorEspecializacaoERegiao(@Param("especializacao") String especializacao,
                                                 @Param("regiao") String regiao);

    // Para autenticação ou verificação de usuário por email
    Optional<Usuario> findByEmail(String email);

    // Para o dashboard
    long countByAtivoTrue();
    long countByAtivoFalse();


    // Para distribuição de leads com enum AreaDireito
    @Query("SELECT u FROM Usuario u WHERE u.ativo = true AND u.especializacao = :especializacao AND LOWER(u.regiao) = LOWER(:regiao)")
    List<Usuario> findByEspecializacaoAndRegiao(@Param("especializacao") AreaDireito especializacao,
                                                @Param("regiao") String regiao);
}
