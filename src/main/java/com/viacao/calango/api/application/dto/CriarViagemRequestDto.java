package com.viacao.calango.api.application.dto;

import com.viacao.calango.api.domain.enums.StatusViagem;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CriarViagemRequestDto(
        @NotNull Long rotaId,
        @NotNull Long onibusId,
        @NotNull LocalDateTime dataHoraSaida,
        LocalDateTime dataHoraChegada
) {}
