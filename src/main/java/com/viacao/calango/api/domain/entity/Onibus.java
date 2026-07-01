package com.viacao.calango.api.domain.entity;

import com.viacao.calango.api.domain.enums.StatusOnibus;
import com.viacao.calango.api.domain.enums.TipoOnibus;
import com.viacao.calango.api.domain.exception.RegraNegocioException;
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
    @Column(nullable = false)
    private StatusOnibus status = StatusOnibus.DISPONIVEL;

    public boolean precisaRevisao() {
        return quilometragemDesdeUltimaRevisao != null && quilometragemDesdeUltimaRevisao >= 10000.0;
    }

    public void setCapacidade(Integer capacidade) {
        if (capacidade != 23 && capacidade != 28 && capacidade != 32) {
            throw new RegraNegocioException("A capacidade do ônibus deve ser estritamente de 23, 28 ou 32 lugares.");
        }
        this.capacidade = capacidade;
    }
}