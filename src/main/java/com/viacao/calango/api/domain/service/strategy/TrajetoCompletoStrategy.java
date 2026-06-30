package com.viacao.calango.api.domain.service.strategy;

import com.viacao.calango.api.domain.entity.Viagem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class TrajetoCompletoStrategy implements PrecoStrategy {

    @Value("${calango.precificacao.desconto-trajeto-completo}")
    private double percentualDesconto;

    @Override
    public BigDecimal calcular(BigDecimal precoAtual, Viagem viagem, LocalDateTime dataCompra, boolean isTrajetoCompleto) {
        if (isTrajetoCompleto) {
            BigDecimal multiplicador = BigDecimal.ONE.subtract(BigDecimal.valueOf(percentualDesconto));
            return precoAtual.multiply(multiplicador);
        }
        return precoAtual;
    }
}