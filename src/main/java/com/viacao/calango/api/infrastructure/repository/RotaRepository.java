package com.viacao.calango.api.infrastructure.repository;

import com.viacao.calango.api.domain.entity.Rota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RotaRepository extends JpaRepository<Rota, Long> {
}