package com.viacao.calango.api.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "calango.precificacao")
@Data
public class PricingConfig {
    private Integer diasAntecedencia;
    private Double descontoAntecedencia;
    private Double descontoTrajetoCompleto;
    private Double fatorLeito;
    private Double fatorSemiLeito;
    private Double fatorExecutivo;
}