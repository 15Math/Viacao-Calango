package com.viacao.calango.api.infrastructure.repository;

import com.viacao.calango.api.domain.entity.Rota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RotaRepository extends JpaRepository<Rota, Long> {

    @Query("SELECT r FROM Rota r LEFT JOIN FETCH r.itinerario i LEFT JOIN FETCH i.parada")
    List<Rota> findAllComItinerario();

    @Query("SELECT r FROM Rota r LEFT JOIN FETCH r.itinerario i LEFT JOIN FETCH i.parada WHERE r.id = :id")
    Optional<Rota> findComItinerarioById(Long id);
}