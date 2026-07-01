package com.viacao.calango.api.application.dto;

import com.viacao.calango.api.domain.enums.TipoOnibus;

public record SugestaoOnibusDto(
        Long onibusId,
        String placa,
        TipoOnibus tipo,
        Integer capacidade,
        int lugaresVaziosEstimados,
        String recomendacao
) {}
