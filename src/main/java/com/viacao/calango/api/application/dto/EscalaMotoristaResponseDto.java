package com.viacao.calango.api.application.dto;

import com.viacao.calango.api.domain.entity.EscalaMotorista;

public record EscalaMotoristaResponseDto(
        Long id,
        Long motoristaId,
        String motoristaNome,
        Long paradaInicioId,
        String paradaInicioNome,
        Long paradaFimId,
        String paradaFimNome
) {
    public static EscalaMotoristaResponseDto fromEntity(EscalaMotorista escala) {
        return new EscalaMotoristaResponseDto(
                escala.getId(),
                escala.getMotorista().getId(),
                escala.getMotorista().getNome(),
                escala.getParadaInicio().getId(),
                escala.getParadaInicio().getNome(),
                escala.getParadaFim().getId(),
                escala.getParadaFim().getNome()
        );
    }
}
