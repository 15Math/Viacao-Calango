package com.viacao.calango.api.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GuicheRequestDto(
        @NotBlank String nome,
        @NotBlank String cidade,
        @NotBlank String estado,
        @NotNull Long paradaId
) {}
