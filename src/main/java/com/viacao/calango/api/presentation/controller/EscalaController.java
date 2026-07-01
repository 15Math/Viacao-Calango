package com.viacao.calango.api.presentation.controller;

import com.viacao.calango.api.application.dto.*;
import com.viacao.calango.api.application.usecase.*;
import com.viacao.calango.api.domain.entity.Motorista;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/escalas")
@RequiredArgsConstructor
public class EscalaController {

    private final AlocarMotoristaUseCase alocarMotoristaUseCase;
    private final AlocarMotoristasAutomaticoUseCase alocarMotoristasAutomaticoUseCase;
    private final GerenciarMotoristaUseCase gerenciarMotoristaUseCase;

    @GetMapping("/motoristas")
    public ResponseEntity<List<MotoristaDetalheDto>> listarMotoristas() {
        return ResponseEntity.ok(gerenciarMotoristaUseCase.listar());
    }

    @PostMapping("/motoristas")
    public ResponseEntity<MotoristaDetalheDto> criarMotorista(@Valid @RequestBody MotoristaRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gerenciarMotoristaUseCase.criar(request));
    }

    @PostMapping("/motoristas/{id}/finalizar-turno")
    public ResponseEntity<MotoristaDetalheDto> finalizarTurno(@PathVariable Long id) {
        return ResponseEntity.ok(gerenciarMotoristaUseCase.finalizarTurno(id));
    }

    @GetMapping("/motoristas-disponiveis")
    public ResponseEntity<List<MotoristaResponseDto>> listarDisponiveis() {
        List<Motorista> disponiveis = alocarMotoristaUseCase.listarMotoristasDisponiveis();
        List<MotoristaResponseDto> response = disponiveis.stream()
                .map(MotoristaResponseDto::fromEntity)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/alocar-motorista")
    public ResponseEntity<MotoristaResponseDto> alocarMotorista(@Valid @RequestBody AlocarMotoristaRequestDto request) {
        MotoristaResponseDto response = alocarMotoristaUseCase.executar(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/viagens/{viagemId}/alocar-automatico")
    public ResponseEntity<List<EscalaMotoristaResponseDto>> alocarAutomatico(@PathVariable Long viagemId) {
        return ResponseEntity.ok(alocarMotoristasAutomaticoUseCase.executar(viagemId));
    }
}
