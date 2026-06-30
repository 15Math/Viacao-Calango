package com.viacao.calango.api.domain.service;

import com.viacao.calango.api.infrastructure.config.PricingConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class CalculadoraPrecoService {

    private final PricingConfig pricingConfig;

    public BigDecimal calcularPrecoFinal(BigDecimal precoBase, LocalDateTime dataCompra, LocalDateTime dataViagem, String tipoOnibus) {
        BigDecimal precoFinal = precoBase;

        // Desconto por antecedencia
        long diasAntecedencia = ChronoUnit.DAYS.between(dataCompra, dataViagem);
        if (diasAntecedencia >= 30 && pricingConfig.getDescontoAntecedencia30Dias() != null) {
            BigDecimal multiplicadorDesconto = BigDecimal.valueOf(1.0 - pricingConfig.getDescontoAntecedencia30Dias());
            precoFinal = precoFinal.multiply(multiplicadorDesconto);
        }

        // Acrescimo pelo tipo de onibus
        if ("LEITO".equalsIgnoreCase(tipoOnibus) && pricingConfig.getMultiplicadorLeito() != null) {
            precoFinal = precoFinal.multiply(BigDecimal.valueOf(pricingConfig.getMultiplicadorLeito()));
        } else if ("EXECUTIVO".equalsIgnoreCase(tipoOnibus) && pricingConfig.getMultiplicadorExecutivo() != null) {
            precoFinal = precoFinal.multiply(BigDecimal.valueOf(pricingConfig.getMultiplicadorExecutivo()));
        }

        return precoFinal;
    }
}