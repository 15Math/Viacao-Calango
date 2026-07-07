package com.viacao.calango.api.application.dto;

import jakarta.validation.constraints.NotBlank;

public record ParadaRequestDto(
        @NotBlank String nome,
        @NotBlank String cidade,
        @NotBlank String estado
) {}