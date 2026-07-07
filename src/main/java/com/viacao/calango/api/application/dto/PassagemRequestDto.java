package com.viacao.calango.api.application.dto;

import com.viacao.calango.api.domain.enums.TipoPagamento;
import jakarta.validation.constraints.NotNull;

public record PassagemRequestDto(
        @NotNull Long viagemId,
        @NotNull Long origemId,
        @NotNull Long destinoId,
        @NotNull TipoPagamento tipoPagamento,
        Integer numeroAssento
) {}
