package com.viacao.calango.api.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "rota_parada")
@Data
public class RotaParada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rota_id", nullable = false)
    private Rota rota;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parada_id", nullable = false)
    private Parada parada;

    @Column(name = "ordem_parada", nullable = false)
    private Integer ordemParada;

    @Column(name = "distancia_proxima_parada_km")
    private Double distanciaProximaParadaKm;

    @Column(name = "parada_troca_motorista")
    private Boolean paradaTrocaMotorista = false;
}