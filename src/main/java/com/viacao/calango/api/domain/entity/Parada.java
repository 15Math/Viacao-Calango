package com.viacao.calango.api.domain.entity;

import jakarta.persistence.*;
import lombok.Data; // Esta anotação cria automaticamente os getters e setters

@Data
@Entity
@Table(name = "parada")
public class Parada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false)
    private String estado;
}