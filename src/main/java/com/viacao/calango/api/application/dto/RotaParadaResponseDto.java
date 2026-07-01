package com.viacao.calango.api.application.dto;

import com.viacao.calango.api.domain.entity.RotaParada;

public record RotaParadaResponseDto(
        Long paradaId,
        String paradaNome,
        Integer ordemParada,
        Double distanciaProximaParadaKm,
        Boolean paradaTrocaMotorista
) {
    public static RotaParadaResponseDto fromEntity(RotaParada rp) {
        return new RotaParadaResponseDto(
                rp.getParada().getId(),
                rp.getParada().getNome(),
                rp.getOrdemParada(),
                rp.getDistanciaProximaParadaKm(),
                Boolean.TRUE.equals(rp.getParadaTrocaMotorista())
        );
    }
}
