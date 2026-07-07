package com.viacao.calango.api.domain.entity;

import com.viacao.calango.api.domain.enums.StatusAssento;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ocupacao_assento")
@Data
public class OcupacaoAssento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "viagem_id", nullable = false)
    private Viagem viagem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origem_segmento_id", nullable = false)
    private Parada origemSegmento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destino_segmento_id", nullable = false)
    private Parada destinoSegmento;

    @Column(name = "numero_assento", nullable = false)
    private Integer numeroAssento;

    @Column(name = "ordem_segmento", nullable = false)
    private Integer ordemSegmento;

    @Enumerated(EnumType.STRING)
    @Column(name = "\"status\"", nullable = false)
    private StatusAssento status = StatusAssento.LIVRE;

    @Version
    private Integer versao;
}