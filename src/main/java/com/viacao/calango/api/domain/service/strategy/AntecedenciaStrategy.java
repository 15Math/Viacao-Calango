package com.viacao.calango.api.domain.service.strategy;

import com.viacao.calango.api.domain.entity.Viagem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class AntecedenciaStrategy implements PrecoStrategy {

    @Value("${calango.precificacao.dias-antecedencia}")
    private int diasParametro;

    @Value("${calango.precificacao.desconto-antecedencia}")
    private double percentualDesconto;

    @Override
    public BigDecimal calcular(BigDecimal precoAtual, Viagem viagem, LocalDateTime dataCompra, boolean isTrajetoCompleto) {
        long diasAntecedencia = ChronoUnit.DAYS.between(dataCompra, viagem.getDataHoraSaida());
        if (diasAntecedencia >= diasParametro) {
            BigDecimal multiplicador = BigDecimal.ONE.subtract(BigDecimal.valueOf(percentualDesconto));
            return precoAtual.multiply(multiplicador);
        }
        return precoAtual;
    }
}