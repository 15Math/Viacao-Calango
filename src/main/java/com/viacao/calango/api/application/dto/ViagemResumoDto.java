package com.viacao.calango.api.application.dto;

import com.viacao.calango.api.domain.entity.Viagem;
import com.viacao.calango.api.domain.enums.StatusViagem;
import com.viacao.calango.api.domain.enums.TipoOnibus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public record ViagemResumoDto(
        Long id,
        String rotaNome,
        String onibusPlaca,
        TipoOnibus tipoOnibus,
        Integer capacidade,
        LocalDateTime dataHoraSaida,
        StatusViagem status,
        String motoristas,
        long assentosVendidos,
        Integer assentosDisponiveis,
        BigDecimal precoBaseRota
) {
    public static ViagemResumoDto fromEntity(Viagem viagem, long assentosVendidos, int assentosDisponiveis) {
        String motoristas = "Não alocado";
        if (viagem.getEscalas() != null && !viagem.getEscalas().isEmpty()) {
            motoristas = viagem.getEscalas().stream()
                    .map(e -> e.getMotorista().getNome())
                    .distinct()
                    .collect(Collectors.joining(", "));
        }
        return new ViagemResumoDto(
                viagem.getId(),
                viagem.getRota().getNome(),
                viagem.getOnibus().getPlaca(),
                viagem.getOnibus().getTipo(),
                viagem.getOnibus().getCapacidade(),
                viagem.getDataHoraSaida(),
                viagem.getStatus(),
                motoristas,
                assentosVendidos,
                assentosDisponiveis,
                viagem.getRota().getPrecoBase()
        );
    }
}
