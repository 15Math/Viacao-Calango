package com.viacao.calango.api.infrastructure.repository;

import com.viacao.calango.api.domain.entity.ConfiguracaoSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfiguracaoSistemaRepository extends JpaRepository<ConfiguracaoSistema, Long> {

    Optional<ConfiguracaoSistema> findByChave(String chave);
}