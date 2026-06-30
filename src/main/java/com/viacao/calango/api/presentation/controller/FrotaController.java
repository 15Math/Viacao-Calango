package com.viacao.calango.api.presentation.controller;

import com.viacao.calango.api.application.usecase.ControlarRevisaoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/frota")
@RequiredArgsConstructor
public class FrotaController {

    private final ControlarRevisaoUseCase controlarRevisaoUseCase;

    @PostMapping("/{onibusId}/fim-viagem")
    public ResponseEntity<Void> registrarFimViagem(@PathVariable Long onibusId, @RequestParam Double kmRodados) {
        controlarRevisaoUseCase.registrarFimViagem(onibusId, kmRodados);
        return ResponseEntity.ok().build();
    }
}