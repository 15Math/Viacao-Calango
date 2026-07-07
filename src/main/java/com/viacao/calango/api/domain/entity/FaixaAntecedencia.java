package com.viacao.calango.api.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "faixa_antecedencia")
@Data
public class FaixaAntecedencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dias_minimos", nullable = false)
    private Integer diasMinimos;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal desconto;
}