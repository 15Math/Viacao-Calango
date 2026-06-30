package com.viacao.calango.api.application.dto;

public record AlocarMotoristaRequestDto(
        Double duracaoEstimadaTrecho,
        Double kmTrecho
) {}