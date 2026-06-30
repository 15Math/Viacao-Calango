package com.viacao.calango.api.application.dto;

import com.viacao.calango.api.domain.entity.Motorista;

public record MotoristaResponseDto(Long id, String nome, String cnh) {
    public static MotoristaResponseDto fromEntity(Motorista motorista) {
        return new MotoristaResponseDto(motorista.getId(), motorista.getNome(), motorista.getCnh());
    }
}