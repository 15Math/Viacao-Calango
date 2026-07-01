package com.viacao.calango.api.presentation.controller;

import com.viacao.calango.api.application.dto.RotaRequestDto;
import com.viacao.calango.api.application.dto.RotaResponseDto;
import com.viacao.calango.api.application.usecase.GerenciarRotaUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rotas")
@RequiredArgsConstructor
public class RotasController {

    private final GerenciarRotaUseCase gerenciarRotaUseCase;

    @GetMapping
    public ResponseEntity<List<RotaResponseDto>> listar() {
        return ResponseEntity.ok(gerenciarRotaUseCase.listar());
    }

    @PostMapping
    public ResponseEntity<RotaResponseDto> criar(@Valid @RequestBody RotaRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gerenciarRotaUseCase.criar(request));
    }
}
