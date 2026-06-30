package com.viacao.calango.api.domain.entity;

import com.viacao.calango.api.domain.enums.StatusMotorista;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Motorista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cnh;
    private LocalDateTime fimUltimoTurno;
    private Double horasDirigidasHoje;
    private Double kmDirigidosHoje;

    @Enumerated(EnumType.STRING)
    private StatusMotorista status;
}