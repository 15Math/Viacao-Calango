package com.viacao.calango.api.domain.service.strategy;

import com.viacao.calango.api.domain.entity.Viagem;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PrecoStrategy {
    BigDecimal calcular(BigDecimal precoAtual, Viagem viagem, LocalDateTime dataCompra, boolean isTrajetoCompleto);
}