package com.viacao.calango.api.application.dto;

import jakarta.validation.constraints.NotBlank;

public record MotoristaRequestDto(
        @NotBlank String nome,
        @NotBlank String cnh
) {}
