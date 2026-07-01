package com.viacao.calango.api.infrastructure.repository;

import com.viacao.calango.api.domain.entity.OcupacaoAssento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OcupacaoAssentoRepository extends JpaRepository<OcupacaoAssento, Long> {

    long countByViagemId(Long viagemId);

    void deleteByViagemId(Long viagemId);

    @Query("""
        SELECT o.numeroAssento FROM OcupacaoAssento o 
        WHERE o.viagem.id = :viagemId 
        AND o.status = 'LIVRE' 
        AND o.ordemSegmento >= :ordemInicio 
        AND o.ordemSegmento < :ordemFim 
        GROUP BY o.numeroAssento 
        HAVING COUNT(o.id) = :totalSegmentos
        ORDER BY o.numeroAssento ASC
    """)
    List<Integer> findAssentosDisponiveisNoTrecho(
            @Param("viagemId") Long viagemId,
            @Param("ordemInicio") Integer ordemInicio,
            @Param("ordemFim") Integer ordemFim,
            @Param("totalSegmentos") Long totalSegmentos
    );

    @Query("SELECT o FROM OcupacaoAssento o WHERE o.viagem.id = :viagemId AND o.numeroAssento = :numeroAssento")
    List<OcupacaoAssento> findByViagemAndAssento(@Param("viagemId") Long viagemId, @Param("numeroAssento") Integer numeroAssento);
}
