package com.viacao.calango.api.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "configuracao_sistema")
public class ConfiguracaoSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chave", unique = true, nullable = false)
    private String chave;

    @Column(name = "valor", nullable = false)
    private String valor;

    @Column(name = "descricao")
    private String descricao;
}