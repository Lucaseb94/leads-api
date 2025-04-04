package com.advocacia.leads.infrastructure.repositories;

import com.advocacia.leads.domain.Lead;
import com.advocacia.leads.domain.AreaDireito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {
    List<Lead> findByAreaInteresse(AreaDireito areaInteresse);
}

