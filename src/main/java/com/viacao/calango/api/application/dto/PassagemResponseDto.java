package com.viacao.calango.api.application.dto;

import com.viacao.calango.api.domain.entity.Passagem;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PassagemResponseDto(
        Long id,
        ViagemResponseDto viagem,
        String origemNome,
        String destinoNome,
        Integer numeroAssento,
        BigDecimal valorPago,
        LocalDateTime dataCompra
) {
    public static PassagemResponseDto fromEntity(Passagem passagem) {
        return new PassagemResponseDto(
                passagem.getId(),
                ViagemResponseDto.fromEntity(passagem.getViagem()),
                passagem.getOrigem().getNome(),
                passagem.getDestino().getNome(),
                passagem.getNumeroAssento(),
                passagem.getValorPago(),
                passagem.getDataCompra()
        );
    }
}