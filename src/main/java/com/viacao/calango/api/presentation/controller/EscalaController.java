package com.viacao.calango.api.presentation.controller;

import com.viacao.calango.api.application.dto.AlocarMotoristaRequestDto;
import com.viacao.calango.api.application.dto.MotoristaResponseDto;
import com.viacao.calango.api.application.usecase.AlocarMotoristaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}