package com.viacao.calango.api.application.dto;

public record AlocarMotoristaRequestDto(
        Long viagemId,
        Long motoristaId,
        Long paradaInicioId,
        Long paradaFimId,
        Double duracaoEstimadaTrecho,
        Double kmTrecho
) {}