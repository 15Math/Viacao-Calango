package com.viacao.calango.api.presentation.controller;

import com.viacao.calango.api.application.dto.ParadaRequestDto;
import com.viacao.calango.api.application.dto.ParadaResponseDto;
import com.viacao.calango.api.application.usecase.GerenciarParadaUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paradas")
@RequiredArgsConstructor
public class ParadasController {

    private final GerenciarParadaUseCase gerenciarParadaUseCase;

    @GetMapping
    public ResponseEntity<List<ParadaResponseDto>> listar() {
        return ResponseEntity.ok(gerenciarParadaUseCase.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParadaResponseDto> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(gerenciarParadaUseCase.buscar(id));
    }

    @PostMapping
    public ResponseEntity<ParadaResponseDto> criar(@Valid @RequestBody ParadaRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gerenciarParadaUseCase.criar(request));
    }
}
