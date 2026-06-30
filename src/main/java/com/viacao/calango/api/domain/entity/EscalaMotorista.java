package com.viacao.calango.api.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "escala_motorista")
@Data
public class EscalaMotorista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "viagem_id", nullable = false)
    private Viagem viagem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "motorista_id", nullable = false)
    private Motorista motorista;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parada_inicio_id", nullable = false)
    private Parada paradaInicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parada_fim_id", nullable = false)
    private Parada paradaFim;
}