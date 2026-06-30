package com.viacao.calango.api.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "rota")
@Data
public class Rota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;
}