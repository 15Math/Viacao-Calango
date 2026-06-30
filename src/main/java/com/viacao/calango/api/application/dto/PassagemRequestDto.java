package com.viacao.calango.api.application.dto;

import com.viacao.calango.api.domain.enums.TipoPagamento;

public record PassagemRequestDto(
        Long viagemId,
        Long origemId,
        Long destinoId,
        TipoPagamento tipoPagamento
) {}