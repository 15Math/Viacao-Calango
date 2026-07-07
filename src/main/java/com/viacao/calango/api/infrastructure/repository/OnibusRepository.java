package com.viacao.calango.api.infrastructure.repository;

import com.viacao.calango.api.domain.entity.Onibus;
import com.viacao.calango.api.domain.enums.StatusOnibus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OnibusRepository extends JpaRepository<Onibus, Long> {
    List<Onibus> findByStatus(StatusOnibus status);

    boolean existsByPlaca(@NotBlank(message = "A placa é obrigatória.") @Pattern(regexp = "^[A-Z]{3}-\\d{4}$", message = "A placa deve seguir o padrão ABC-1234.") String placa);
}