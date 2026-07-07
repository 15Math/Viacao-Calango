package com.viacao.calango.api.domain.service.strategy;

import com.viacao.calango.api.domain.entity.FaixaAntecedencia;
import com.viacao.calango.api.domain.entity.Viagem;
import com.viacao.calango.api.infrastructure.repository.FaixaAntecedenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

@Component
@RequiredArgsConstructor
public class AntecedenciaStrategy implements PrecoStrategy {

    private final FaixaAntecedenciaRepository faixaRepository;

    @Override
    public BigDecimal calcular(BigDecimal precoAtual, Viagem viagem, LocalDateTime dataCompra, boolean isTrajetoCompleto) {
        long diasAntecedencia = ChronoUnit.DAYS.between(dataCompra.toLocalDate(), viagem.getDataHoraSaida().toLocalDate());

        if (diasAntecedencia < 0) {
            return precoAtual;
        }

        // Busca todas as faixas do banco de dados e encontra a maior faixa aplicavel
        return faixaRepository.findAll().stream()
                .filter(f -> f.getDiasMinimos() != null && diasAntecedencia >= f.getDiasMinimos())
                .max(Comparator.comparing(FaixaAntecedencia::getDiasMinimos))
                .map(faixa -> precoAtual.multiply(BigDecimal.ONE.subtract(faixa.getDesconto()))
                        .setScale(2, RoundingMode.HALF_UP))
                .orElse(precoAtual);
    }
}
