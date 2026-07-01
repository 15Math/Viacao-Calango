package com.viacao.calango.api.application.dto;

import com.viacao.calango.api.domain.entity.Viagem;
import com.viacao.calango.api.domain.enums.StatusViagem;
import com.viacao.calango.api.domain.enums.TipoOnibus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ViagemDetalheDto(
        Long id,
        Long rotaId,
        String rotaNome,
        Long onibusId,
        String onibusPlaca,
        TipoOnibus tipoOnibus,
        Integer capacidade,
        LocalDateTime dataHoraSaida,
        LocalDateTime dataHoraChegada,
        StatusViagem status,
        List<RotaParadaResponseDto> itinerario,
        List<EscalaMotoristaResponseDto> escalasMotoristas,
        long totalPassagens
) {
    public static ViagemDetalheDto fromEntity(Viagem viagem, long totalPassagens) {
        List<RotaParadaResponseDto> itinerario = viagem.getRota().getItinerario().stream()
                .map(RotaParadaResponseDto::fromEntity)
                .toList();
        List<EscalaMotoristaResponseDto> escalas = viagem.getEscalas() != null
                ? viagem.getEscalas().stream().map(EscalaMotoristaResponseDto::fromEntity).toList()
                : List.of();
        return new ViagemDetalheDto(
                viagem.getId(),
                viagem.getRota().getId(),
                viagem.getRota().getNome(),
                viagem.getOnibus().getId(),
                viagem.getOnibus().getPlaca(),
                viagem.getOnibus().getTipo(),
                viagem.getOnibus().getCapacidade(),
                viagem.getDataHoraSaida(),
                viagem.getDataHoraChegada(),
                viagem.getStatus(),
                itinerario,
                escalas,
                totalPassagens
        );
    }
}
