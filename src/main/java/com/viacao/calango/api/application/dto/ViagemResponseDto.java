package com.viacao.calango.api.application.dto;

import com.viacao.calango.api.domain.entity.Viagem;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public record ViagemResponseDto(Long id, String onibusPlaca, String motoristaNome, LocalDateTime dataHoraSaida) {
    public static ViagemResponseDto fromEntity(Viagem viagem) {
        String motoristas = "Não Alocado";
        if (viagem.getEscalas() != null && !viagem.getEscalas().isEmpty()) {
            motoristas = viagem.getEscalas().stream()
                    .map(escala -> escala.getMotorista().getNome())
                    .distinct()
                    .collect(Collectors.joining(", "));
        }
        return new ViagemResponseDto(viagem.getId(), viagem.getOnibus().getPlaca(), motoristas, viagem.getDataHoraSaida());
    }
}