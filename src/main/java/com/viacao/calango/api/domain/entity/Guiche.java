package com.viacao.calango.api.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "guiche")
@Data
public class Guiche {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String cidade;
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parada_id")
    private Parada parada;

    private Boolean ativo = true;
}
