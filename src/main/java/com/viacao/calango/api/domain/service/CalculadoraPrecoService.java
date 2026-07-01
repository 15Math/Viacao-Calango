package com.viacao.calango.api.domain.service;

import com.viacao.calango.api.domain.entity.Rota;
import com.viacao.calango.api.domain.entity.RotaParada;
import com.viacao.calango.api.domain.entity.Viagem;
import com.viacao.calango.api.domain.exception.RegraNegocioException;
import com.viacao.calango.api.domain.service.strategy.PrecoStrategy;
import com.viacao.calango.api.infrastructure.config.PricingConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalculadoraPrecoService {

    private final List<PrecoStrategy> estrategias;
    private final PricingConfig pricingConfig;

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
                                         String tipoOnibus, boolean isTrajetoCompleto) {
        BigDecimal precoFinal = precoBaseTrecho;

        for (PrecoStrategy estrategia : estrategias) {
            precoFinal = estrategia.calcular(precoFinal, viagem, dataCompra, isTrajetoCompleto);
        }

        precoFinal = aplicarFatorTipoOnibus(precoFinal, tipoOnibus);
        return precoFinal.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal aplicarFatorTipoOnibus(BigDecimal preco, String tipoOnibus) {
        if ("LEITO".equalsIgnoreCase(tipoOnibus) && pricingConfig.getFatorLeito() != null) {
            return preco.multiply(BigDecimal.valueOf(pricingConfig.getFatorLeito()));
        }
        if ("SEMI_LEITO".equalsIgnoreCase(tipoOnibus) && pricingConfig.getFatorSemiLeito() != null) {
            return preco.multiply(BigDecimal.valueOf(pricingConfig.getFatorSemiLeito()));
        }
        if ("EXECUTIVO".equalsIgnoreCase(tipoOnibus) && pricingConfig.getFatorExecutivo() != null) {
            return preco.multiply(BigDecimal.valueOf(pricingConfig.getFatorExecutivo()));
        }
        return preco;
    }
}
