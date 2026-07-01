package com.viacao.calango.api.application.dto;

import com.viacao.calango.api.domain.entity.Guiche;

public record GuicheResponseDto(Long id, String nome, String cidade, String estado, Long paradaId, String paradaNome) {
    public static GuicheResponseDto fromEntity(Guiche guiche) {
        return new GuicheResponseDto(
                guiche.getId(),
                guiche.getNome(),
                guiche.getCidade(),
                guiche.getEstado(),
                guiche.getParada() != null ? guiche.getParada().getId() : null,
                guiche.getParada() != null ? guiche.getParada().getNome() : null
        );
    }
}
