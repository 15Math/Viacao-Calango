package com.viacao.calango.api.domain.entity;

import com.viacao.calango.api.domain.enums.StatusOnibus;
import com.viacao.calango.api.domain.enums.TipoOnibus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "onibus")
@Data
public class Onibus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String placa;

    private Integer capacidade;

    @Enumerated(EnumType.STRING)
    private TipoOnibus tipo;

    @Column(name = "quilometragem_total")
    private Double quilometragemTotal;

    @Column(name = "quilometragem_desde_ultima_revisao")
    private Double quilometragemDesdeUltimaRevisao;

    @Enumerated(EnumType.STRING)
    @Column(name = "\"status\"", nullable = false)
    private StatusOnibus status = StatusOnibus.DISPONIVEL;

    public boolean precisaRevisao() {
        return quilometragemDesdeUltimaRevisao != null && quilometragemDesdeUltimaRevisao >= 10000.0;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }
}