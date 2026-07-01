package com.viacao.calango.api.domain.service.strategy;

import com.viacao.calango.api.domain.entity.Viagem;
import com.viacao.calango.api.infrastructure.config.PricingConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class AntecedenciaStrategy implements PrecoStrategy {

    private final PricingConfig pricingConfig;

    @Override
    public BigDecimal calcular(BigDecimal precoAtual, Viagem viagem, LocalDateTime dataCompra, boolean isTrajetoCompleto) {
        long diasAntecedencia = ChronoUnit.DAYS.between(dataCompra.toLocalDate(), viagem.getDataHoraSaida().toLocalDate());
        if (diasAntecedencia < 0) {
            return precoAtual;
        }
        return pricingConfig.descontoParaDiasAntecedencia(diasAntecedencia)
                .map(desconto -> precoAtual.multiply(BigDecimal.ONE.subtract(BigDecimal.valueOf(desconto)))
                        .setScale(2, RoundingMode.HALF_UP))
                .orElse(precoAtual);
    }
}
