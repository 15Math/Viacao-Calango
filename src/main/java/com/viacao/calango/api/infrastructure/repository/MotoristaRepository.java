package com.viacao.calango.api.infrastructure.repository;

import com.viacao.calango.api.domain.entity.Motorista;
import com.viacao.calango.api.domain.enums.StatusMotorista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MotoristaRepository extends JpaRepository<Motorista, Long> {
    List<Motorista> findByStatus(StatusMotorista status);
}