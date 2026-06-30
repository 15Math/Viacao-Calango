package com.viacao.calango.api.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ocupacao_assento")
@Data
public class OcupacaoAssento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "viagem_id", nullable = false)
    private Viagem viagem;

    @ManyToOne
    @JoinColumn(name = "origem_segmento_id", nullable = false)
    private Parada origemSegmento;

    @ManyToOne
    @JoinColumn(name = "destino_segmento_id", nullable = false)
    private Parada destinoSegmento;

    @Column(name = "numero_assento", nullable = false)
    private Integer numeroAssento;

    @Column(name = "ordem_segmento", nullable = false)
    private Integer ordemSegmento; // Define a posição do trecho na sequência da rota

    @Column(nullable = false)
    private String status; // LIVRE, OCUPADO

    @Version
    private Integer versao;
}