package com.viacao.calango.api.infrastructure.repository;

import com.viacao.calango.api.domain.entity.Viagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViagemRepository extends JpaRepository<Viagem, Long> {
}