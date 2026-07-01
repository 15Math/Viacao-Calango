package com.viacao.calango.api.application.dto;

import com.viacao.calango.api.domain.enums.TipoOnibus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OnibusRequestDto(
        @NotBlank String placa,
        @NotNull Integer capacidade,
        @NotNull TipoOnibus tipo
) {}
