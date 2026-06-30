package com.viacao.calango.api.presentation.controller;

import com.viacao.calango.api.application.dto.AlocarMotoristaRequestDto;
import com.viacao.calango.api.application.dto.MotoristaResponseDto;
import com.viacao.calango.api.application.usecase.AlocarMotoristaUseCase;
import com.viacao.calango.api.domain.entity.Motorista;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/escalas")
@RequiredArgsConstructor
public class EscalaController {

    private final AlocarMotoristaUseCase alocarMotoristaUseCase;

    @PostMapping("/alocar-motorista")
    public ResponseEntity<MotoristaResponseDto> alocarMotorista(@RequestBody AlocarMotoristaRequestDto request) {
        MotoristaResponseDto response = alocarMotoristaUseCase.executar(request);
        return ResponseEntity.ok(response);
    }

    // Endpoint para funcionários do guichê monitorarem se há motoristas disponíveis de prontidão
    @GetMapping("/motoristas-disponiveis")
    public ResponseEntity<List<MotoristaResponseDto>> listarDisponiveis() {
        List<Motorista> disponiveis = alocarMotoristaUseCase.listarMotoristasDisponiveis();
        List<MotoristaResponseDto> response = disponiveis.stream()
                .map(MotoristaResponseDto::fromEntity)
                .toList();
        return ResponseEntity.ok(response);
    }
}