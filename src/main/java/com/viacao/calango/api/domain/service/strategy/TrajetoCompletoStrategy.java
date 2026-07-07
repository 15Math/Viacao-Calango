package com.viacao.calango.api.domain.service.strategy;

import com.viacao.calango.api.domain.entity.Viagem;
import com.viacao.calango.api.infrastructure.repository.ConfiguracaoSistemaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrajetoCompletoStrategy implements PrecoStrategy {

    private final ConfiguracaoSistemaRepository configuracaoRepository;

    @Override
    public BigDecimal calcular(BigDecimal precoAtual, Viagem viagem, LocalDateTime dataCompra, boolean isTrajetoCompleto) {
        if (isTrajetoCompleto) {
            double percentualDesconto = configuracaoRepository.findByChave("DESCONTO_TRAJETO_COMPLETO")
                    .map(config -> Double.parseDouble(config.getValor()))
                    .orElseGet(() -> {
                        log.error("CRITICO: Chave de precificação 'DESCONTO_TRAJETO_COMPLETO' ausente no banco de dados!");
                        return 0.0;
                    });

            BigDecimal multiplicador = BigDecimal.ONE.subtract(BigDecimal.valueOf(percentualDesconto));
            return precoAtual.multiply(multiplicador).setScale(2, RoundingMode.HALF_UP);
        }
        return precoAtual;
    }
}