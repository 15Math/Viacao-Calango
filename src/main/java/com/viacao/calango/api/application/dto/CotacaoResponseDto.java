package com.viacao.calango.api.application.dto;

import java.math.BigDecimal;
import java.util.List;

public record CotacaoResponseDto(
        Long viagemId,
        Long origemId,
        String origemNome,
        Long destinoId,
        String destinoNome,
        BigDecimal precoBaseTrecho,
        BigDecimal precoFinal,
        boolean trajetoCompleto,
        List<String> descontosAplicados
) {}
