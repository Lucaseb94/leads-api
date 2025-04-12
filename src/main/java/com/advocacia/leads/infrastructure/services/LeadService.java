package com.advocacia.leads.infrastructure.services;

import com.advocacia.leads.domain.Lead;
import com.advocacia.leads.domain.AreaDireito;
import com.advocacia.leads.domain.Usuario;
import com.advocacia.leads.dto.LeadDTO;
import com.advocacia.leads.infrastructure.repositories.LeadRepository;
import com.advocacia.leads.infrastructure.repositories.UsuarioRepository;
import com.advocacia.leads.infrastructure.scheduling.DistribuicaoLeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeadService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    private final LeadRepository leadRepository;
    private final DistribuicaoLeadService distribuicaoLeadService;

    public LeadService(LeadRepository leadRepository, DistribuicaoLeadService distribuicaoLeadService) {
        this.leadRepository = leadRepository;
        this.distribuicaoLeadService = distribuicaoLeadService;
    }
    public List<Usuario> buscarAdvogadosPorEspecializacaoERegiao(String especializacao, String regiao) {
        return usuarioRepository.buscarPorEspecializacaoERegiao(
                especializacao.trim().toLowerCase(),
                regiao.trim().toLowerCase()
        );
    }

    @Transactional
    public Lead criarLead(LeadDTO dto) {
        Lead lead = new Lead();
        lead.setNome(dto.nome());
        lead.setEmail(dto.email());
        lead.setTelefone(dto.telefone());
        lead.setRegiao(dto.regiao());
        lead.setEndereco(dto.endereco()); // Novo mapeamento do campo endereço
        lead.setAreaInteresse(dto.areaInteresse()); // Campo já do tipo AreaDireito
        lead.setDataRegistro(LocalDateTime.now());

        Lead leadSalvo = leadRepository.save(lead);

        // Dispara o email imediatamente para os advogados compatíveis;
        // caso ocorra erro no envio, o lead já foi criado.
        try {
            distribuicaoLeadService.distribuirNovoLead(leadSalvo);
        } catch (Exception e) {
            e.printStackTrace(); // ou utilize um logger para registrar o erro
        }

        return leadSalvo;
    }

    @Transactional
    public void atualizar(LeadDTO dto) {
        Lead lead = leadRepository.findById(dto.id())
                .orElseThrow(() -> new RuntimeException("Lead não encontrado"));
        lead.setNome(dto.nome());
        lead.setEmail(dto.email());
        lead.setTelefone(dto.telefone());
        lead.setRegiao(dto.regiao());
        lead.setEndereco(dto.endereco()); // Atualiza também o endereço
        lead.setAreaInteresse(dto.areaInteresse());
        leadRepository.save(lead);
    }

    @Transactional
    public List<Lead> listarPorArea(AreaDireito area) {
        return leadRepository.findAll().stream()
                .filter(lead -> lead.getAreaInteresse() == area)
                .collect(Collectors.toList());
    }
}
