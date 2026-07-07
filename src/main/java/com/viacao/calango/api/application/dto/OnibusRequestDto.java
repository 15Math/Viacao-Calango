package com.viacao.calango.api.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record OnibusRequestDto(
        @NotBlank(message = "A placa é obrigatória.")
        @Pattern(regexp = "^[A-Z]{3}-\\d{4}$", message = "A placa deve seguir o padrão ABC-1234.")
        String placa,

        @NotNull(message = "A capacidade é obrigatória.")
        @Positive(message = "A capacidade deve ser maior que zero.")
        Integer capacidade,

        @NotBlank(message = "O tipo de ônibus é obrigatório.")
        @Pattern(regexp = "^(LEITO|SEMI_LEITO|EXECUTIVO)$", message = "O tipo deve ser LEITO, SEMI_LEITO ou EXECUTIVO.")
        String tipo
) {}