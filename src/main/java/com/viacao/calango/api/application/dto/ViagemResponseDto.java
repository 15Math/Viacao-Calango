package com.viacao.calango.api.application.dto;

import com.viacao.calango.api.domain.entity.Viagem;
import java.time.LocalDateTime;

public record ViagemResponseDto(Long id, String onibusPlaca, String motoristaNome, LocalDateTime dataHoraSaida) {
    public static ViagemResponseDto fromEntity(Viagem viagem) {
        String motorista = (viagem.getMotorista() != null) ? viagem.getMotorista().getNome() : "Não Alocado";
        return new ViagemResponseDto(viagem.getId(), viagem.getOnibus().getPlaca(), motorista, viagem.getDataHoraSaida());
    }
}