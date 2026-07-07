package com.viacao.calango.api.domain.entity;

import com.viacao.calango.api.domain.enums.StatusViagem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "viagens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Viagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rota_id", nullable = false)
    private Rota rota;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "onibus_id", nullable = false)
    private Onibus onibus;

    @OneToMany(mappedBy = "viagem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EscalaMotorista> escalas = new ArrayList<>();

    @OneToMany(mappedBy = "viagem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OcupacaoAssento> ocupacoes = new ArrayList<>();

    @Column(name = "data_hora_saida", nullable = false)
    private LocalDateTime dataHoraSaida;

    @Column(name = "data_hora_chegada")
    private LocalDateTime dataHoraChegada;

    @Enumerated(EnumType.STRING)
    @Column(name = "\"status\"", nullable = false)
    private StatusViagem status = StatusViagem.PROGRAMADA;
}