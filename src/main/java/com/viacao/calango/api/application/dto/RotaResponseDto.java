package com.viacao.calango.api.application.dto;

import com.viacao.calango.api.domain.entity.Rota;

import java.math.BigDecimal;
import java.util.List;

public record RotaResponseDto(
        Long id,
        String nome,
        BigDecimal precoBase,
        List<RotaParadaResponseDto> itinerario
) {
    public static RotaResponseDto fromEntity(Rota rota) {
        List<RotaParadaResponseDto> paradas = rota.getItinerario().stream()
                .map(RotaParadaResponseDto::fromEntity)
                .toList();
        return new RotaResponseDto(rota.getId(), rota.getNome(), rota.getPrecoBase(), paradas);
    }
}
