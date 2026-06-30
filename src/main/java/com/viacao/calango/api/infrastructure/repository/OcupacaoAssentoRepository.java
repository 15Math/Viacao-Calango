package com.viacao.calango.api.infrastructure.repository;

import com.viacao.calango.api.domain.entity.OcupacaoAssento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OcupacaoAssentoRepository extends JpaRepository<OcupacaoAssento, Long> {

    @Query("SELECT o.numeroAssento FROM OcupacaoAssento o WHERE o.viagem.id = :viagemId AND o.status = 'LIVRE' GROUP BY o.numeroAssento")
    List<Integer> findAssentosDisponiveis(@Param("viagemId") Long viagemId);

}