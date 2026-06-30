package com.viacao.calango.api.domain.service;

import com.viacao.calango.api.domain.entity.Viagem;
import com.viacao.calango.api.domain.service.strategy.PrecoStrategy;
import com.viacao.calango.api.infrastructure.config.PricingConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalculadoraPrecoService {

    private final List<PrecoStrategy> estrategias;
    private final PricingConfig pricingConfig;

    public BigDecimal calcularPrecoFinal(BigDecimal precoBase, LocalDateTime dataCompra, Viagem viagem, String tipoOnibus, boolean isTrajetoCompleto) {
        BigDecimal precoFinal = precoBase;

        for (PrecoStrategy estrategia : estrategias) {
            precoFinal = estrategia.calcular(precoFinal, viagem, dataCompra, isTrajetoCompleto);
        }

        if ("LEITO".equalsIgnoreCase(tipoOnibus) && pricingConfig.getFatorLeito() != null) {
            precoFinal = precoFinal.multiply(BigDecimal.valueOf(pricingConfig.getFatorLeito()));
        } else if ("SEMI_LEITO".equalsIgnoreCase(tipoOnibus) && pricingConfig.getFatorSemiLeito() != null) {
            precoFinal = precoFinal.multiply(BigDecimal.valueOf(pricingConfig.getFatorSemiLeito()));
        } else if ("EXECUTIVO".equalsIgnoreCase(tipoOnibus) && pricingConfig.getFatorExecutivo() != null) {
            precoFinal = precoFinal.multiply(BigDecimal.valueOf(pricingConfig.getFatorExecutivo()));
        }

        return precoFinal;
    }
}