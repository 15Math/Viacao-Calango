package com.viacao.calango.api.infrastructure.repository;

import com.viacao.calango.api.domain.entity.Guiche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuicheRepository extends JpaRepository<Guiche, Long> {
    List<Guiche> findByAtivoTrue();
}
