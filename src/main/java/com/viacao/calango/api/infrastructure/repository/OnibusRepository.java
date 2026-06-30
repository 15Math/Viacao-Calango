package com.viacao.calango.api.infrastructure.repository;

import com.viacao.calango.api.domain.entity.Onibus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnibusRepository extends JpaRepository<Onibus, Long> {
}