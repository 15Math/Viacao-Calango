package com.viacao.calango.api.application.dto;

import com.viacao.calango.api.domain.entity.Motorista;
import com.viacao.calango.api.domain.enums.StatusMotorista;

import java.time.LocalDateTime;

public record MotoristaDetalheDto(
        Long id,
        String nome,
        String cnh,
        StatusMotorista status,
        LocalDateTime fimUltimoTurno,
        Double horasDirigidasHoje,
        Double kmDirigidosHoje
) {
    public static MotoristaDetalheDto fromEntity(Motorista motorista) {
        return new MotoristaDetalheDto(
                motorista.getId(),
                motorista.getNome(),
                motorista.getCnh(),
                motorista.getStatus(),
                motorista.getFimUltimoTurno(),
                motorista.getHorasDirigidasHoje(),
                motorista.getKmDirigidosHoje()
        );
    }
}
