package com.viacao.calango.api.application.dto;

import com.viacao.calango.api.domain.entity.Passagem;
import com.viacao.calango.api.domain.enums.TipoPagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PassagemResponseDto(
        Long id,
        Long viagemId,
        String origem,
        String destino,
        Integer numeroAssento,
        BigDecimal valorPago,
        TipoPagamento tipoPagamento,
        String codigoTransacao,
        LocalDateTime dataCompra,
        String guicheNome
) {
    public static PassagemResponseDto fromEntity(Passagem p) {
        return new PassagemResponseDto(
                p.getId(),
                p.getViagem().getId(),
                p.getOrigem().getNome(),
                p.getDestino().getNome(),
                p.getNumeroAssento(),
                p.getValorPago(),
                p.getTipoPagamento(),
                p.getCodigoTransacao(),
                p.getDataCompra(),
                p.getGuiche() != null ? p.getGuiche().getNome() : null
        );
    }
}
