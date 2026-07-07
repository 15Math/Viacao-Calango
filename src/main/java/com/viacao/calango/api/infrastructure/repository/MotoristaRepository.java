package com.viacao.calango.api.infrastructure.repository;

import com.viacao.calango.api.domain.entity.Motorista;
import com.viacao.calango.api.domain.enums.StatusMotorista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MotoristaRepository extends JpaRepository<Motorista, Long> {

    @Query("""
        SELECT m FROM Motorista m 
        WHERE m.status = :status 
        AND (m.fimUltimoTurno IS NULL OR m.fimUltimoTurno <= :limiteDescanso)
    """)
    List<Motorista> findDisponiveisComDescanso(
            @Param("status") StatusMotorista status,
            @Param("limiteDescanso") LocalDateTime limiteDescanso
    );
}