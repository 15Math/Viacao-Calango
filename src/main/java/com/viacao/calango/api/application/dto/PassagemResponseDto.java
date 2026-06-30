package com.viacao.calango.api.application.dto;

import com.viacao.calango.api.domain.entity.Passagem;

public record PassagemResponseDto(
        Long id,
        String origem,
        String destino,
        Integer numeroAssento,
        java.math.BigDecimal valorPago
) {
    public static PassagemResponseDto fromEntity(Passagem p) {
        return new PassagemResponseDto(
                p.getId(),
                p.getOrigem().getNome(),
                p.getDestino().getNome(),
                p.getNumeroAssento(),
                p.getValorPago()
        );
    }
}