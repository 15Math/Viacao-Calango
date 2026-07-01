package com.viacao.calango.api.application.dto;

import com.viacao.calango.api.domain.entity.Parada;

public record ParadaResponseDto(Long id, String nome, String cidade, String estado) {
    public static ParadaResponseDto fromEntity(Parada parada) {
        return new ParadaResponseDto(parada.getId(), parada.getNome(), parada.getCidade(), parada.getEstado());
    }
}
