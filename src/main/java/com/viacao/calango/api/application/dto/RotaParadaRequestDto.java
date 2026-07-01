package com.viacao.calango.api.application.dto;

import jakarta.validation.constraints.NotNull;

public record RotaParadaRequestDto(
        @NotNull Long paradaId,
        @NotNull Integer ordemParada,
        Double distanciaProximaParadaKm,
        Boolean paradaTrocaMotorista
) {}
