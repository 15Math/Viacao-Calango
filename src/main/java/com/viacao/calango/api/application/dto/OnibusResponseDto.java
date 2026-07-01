package com.viacao.calango.api.application.dto;

import com.viacao.calango.api.domain.entity.Onibus;
import com.viacao.calango.api.domain.enums.StatusOnibus;
import com.viacao.calango.api.domain.enums.TipoOnibus;

public record OnibusResponseDto(
        Long id,
        String placa,
        Integer capacidade,
        TipoOnibus tipo,
        Double quilometragemTotal,
        Double quilometragemDesdeUltimaRevisao,
        StatusOnibus status,
        boolean precisaRevisao
) {
    public static OnibusResponseDto fromEntity(Onibus onibus) {
        return new OnibusResponseDto(
                onibus.getId(),
                onibus.getPlaca(),
                onibus.getCapacidade(),
                onibus.getTipo(),
                onibus.getQuilometragemTotal(),
                onibus.getQuilometragemDesdeUltimaRevisao(),
                onibus.getStatus(),
                onibus.precisaRevisao()
        );
    }
}
