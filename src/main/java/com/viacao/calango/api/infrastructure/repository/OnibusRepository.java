package com.viacao.calango.api.infrastructure.repository;

import com.viacao.calango.api.domain.entity.Onibus;
import com.viacao.calango.api.domain.enums.StatusOnibus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OnibusRepository extends JpaRepository<Onibus, Long> {
    List<Onibus> findByStatus(StatusOnibus status);
}