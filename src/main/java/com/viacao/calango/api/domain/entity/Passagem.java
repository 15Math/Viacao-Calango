package com.viacao.calango.api.domain.entity;

import com.viacao.calango.api.domain.enums.TipoPagamento;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "passagem")
@Data
public class Passagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "viagem_id", nullable = false)
    private Viagem viagem;

    @ManyToOne
    @JoinColumn(name = "origem_id", nullable = false)
    private Parada origem;

    @ManyToOne
    @JoinColumn(name = "destino_id", nullable = false)
    private Parada destino;

    @Column(name = "numero_assento")
    private Integer numeroAssento;

    @Column(name = "valor_pago")
    private BigDecimal valorPago;

    @Column(name = "data_compra")
    private LocalDateTime dataCompra;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pagamento")
    private TipoPagamento tipoPagamento;

    @Column(name = "codigo_transacao")
    private String codigoTransacao;
}