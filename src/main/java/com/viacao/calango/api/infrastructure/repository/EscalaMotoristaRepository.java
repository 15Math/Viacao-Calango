package com.viacao.calango.api.infrastructure.repository;

import com.viacao.calango.api.domain.entity.EscalaMotorista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EscalaMotoristaRepository extends JpaRepository<EscalaMotorista, Long> {
    List<EscalaMotorista> findByViagemId(Long viagemId);
}