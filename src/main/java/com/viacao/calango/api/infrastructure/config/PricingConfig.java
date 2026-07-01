package com.viacao.calango.api.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Configuration
@ConfigurationProperties(prefix = "calango.precificacao")
@Data
public class PricingConfig {
    private Double descontoTrajetoCompleto;
    private Double fatorLeito;
    private Double fatorSemiLeito;
    private Double fatorExecutivo;
    private List<FaixaAntecedenciaConfig> faixasAntecedencia = new ArrayList<>();

    public Optional<Double> descontoParaDiasAntecedencia(long dias) {
        return faixasAntecedencia.stream()
                .filter(f -> f.getDiasMinimos() != null && dias >= f.getDiasMinimos())
                .max(Comparator.comparing(FaixaAntecedenciaConfig::getDiasMinimos))
                .map(FaixaAntecedenciaConfig::getDesconto);
    }
}