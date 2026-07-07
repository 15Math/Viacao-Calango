package com.viacao.calango.api.domain.service;

import com.viacao.calango.api.domain.entity.Rota;
import com.viacao.calango.api.domain.entity.RotaParada;
import com.viacao.calango.api.domain.entity.Viagem;
import com.viacao.calango.api.domain.exception.RegraNegocioException;
import com.viacao.calango.api.domain.service.strategy.PrecoStrategy;
import lombok.extern.slf4j.Slf4j;
import com.viacao.calango.api.infrastructure.repository.ConfiguracaoSistemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.viacao.calango.api.domain.enums.TipoOnibus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculadoraPrecoService {

    private final List<PrecoStrategy> estrategias;
    private final ConfiguracaoSistemaRepository configuracaoRepository;

    public BigDecimal calcularPrecoTrecho(Rota rota, int ordemInicio, int ordemFim) {
        List<RotaParada> itinerario = rota.getItinerario();
        double kmTrecho = 0;
        for (int i = ordemInicio; i < ordemFim; i++) {
            Double dist = itinerario.get(i).getDistanciaProximaParadaKm();
            if (dist != null) {
                kmTrecho += dist;
            }
        }
        double kmTotal = itinerario.stream()
                .mapToDouble(rp -> rp.getDistanciaProximaParadaKm() != null ? rp.getDistanciaProximaParadaKm() : 0)
                .sum();
        if (kmTotal <= 0) {
            throw new RegraNegocioException("A rota não possui distâncias cadastradas para cálculo proporcional.");
        }
        BigDecimal proporcao = BigDecimal.valueOf(kmTrecho / kmTotal);
        return rota.getPrecoBase().multiply(proporcao).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularPrecoFinal(BigDecimal precoBaseTrecho, LocalDateTime dataCompra, Viagem viagem,
                                         TipoOnibus tipoOnibus, boolean isTrajetoCompleto) {
        BigDecimal precoFinal = precoBaseTrecho;

        for (PrecoStrategy estrategia : estrategias) {
            precoFinal = estrategia.calcular(precoFinal, viagem, dataCompra, isTrajetoCompleto);
        }

        precoFinal = aplicarFatorTipoOnibus(precoFinal, tipoOnibus);
        return precoFinal.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal aplicarFatorTipoOnibus(BigDecimal preco, TipoOnibus tipoOnibus) {
        if (tipoOnibus == null) return preco;

        String chaveConfig = "FATOR_" + tipoOnibus.name();

        double fator = configuracaoRepository.findByChave(chaveConfig)
                .map(config -> Double.parseDouble(config.getValor()))
                .orElseGet(() -> {
                    log.error("CRITICO: Chave de precificação '{}' ausente no banco de dados!", chaveConfig);
                    return 1.0;
                });

        return preco.multiply(BigDecimal.valueOf(fator));
    }
}