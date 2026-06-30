package com.viacao.calango.api.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "viacao.pricing")
@Data
public class PricingConfig {
    private Double descontoAntecedencia30Dias;
    private Double multiplicadorLeito;
    private Double multiplicadorExecutivo;
}