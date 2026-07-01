package com.viacao.calango.api.infrastructure.repository;

import com.viacao.calango.api.domain.entity.Passagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassagemRepository extends JpaRepository<Passagem, Long> {
    List<Passagem> findByViagemId(Long viagemId);
    long countByViagemId(Long viagemId);
}