package com.viacao.calango.api.infrastructure.repository;

import com.viacao.calango.api.domain.entity.Viagem;
import com.viacao.calango.api.domain.enums.StatusViagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ViagemRepository extends JpaRepository<Viagem, Long> {

    List<Viagem> findByRotaIdAndStatus(Long rotaId, StatusViagem status);

    @Query("""
        SELECT DISTINCT v FROM Viagem v
        JOIN FETCH v.rota r
        JOIN FETCH v.onibus o
        LEFT JOIN FETCH v.escalas e
        LEFT JOIN FETCH e.motorista
        WHERE v.status = :status
        AND v.dataHoraSaida >= :dataInicio
        AND v.dataHoraSaida < :dataFim
        AND EXISTS (
            SELECT rp1 FROM RotaParada rp1
            WHERE rp1.rota = r AND rp1.parada.id = :origemId
        )
        AND EXISTS (
            SELECT rp2 FROM RotaParada rp2
            WHERE rp2.rota = r AND rp2.parada.id = :destinoId
            AND rp2.ordemParada > (
                SELECT MIN(rp3.ordemParada) FROM RotaParada rp3
                WHERE rp3.rota = r AND rp3.parada.id = :origemId
            )
        )
        ORDER BY v.dataHoraSaida ASC
    """)
    List<Viagem> buscarPorTrechoEData(
            @Param("origemId") Long origemId,
            @Param("destinoId") Long destinoId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim,
            @Param("status") StatusViagem status
    );

    @Query("""
        SELECT v FROM Viagem v
        JOIN FETCH v.rota
        JOIN FETCH v.onibus
        LEFT JOIN FETCH v.escalas e
        LEFT JOIN FETCH e.motorista
        WHERE v.id = :id
    """)
    java.util.Optional<Viagem> findDetalhadaById(@Param("id") Long id);
}