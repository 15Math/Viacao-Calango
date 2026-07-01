package com.viacao.calango.api.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record RotaRequestDto(
        @NotBlank String nome,
        @NotNull @Positive BigDecimal precoBase,
        @NotEmpty @Valid List<RotaParadaRequestDto> itinerario
) {}
