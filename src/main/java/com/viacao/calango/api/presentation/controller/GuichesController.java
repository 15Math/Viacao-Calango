package com.viacao.calango.api.presentation.controller;

import com.viacao.calango.api.application.dto.GuicheRequestDto;
import com.viacao.calango.api.application.dto.GuicheResponseDto;
import com.viacao.calango.api.application.usecase.GerenciarGuicheUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guiches")
@RequiredArgsConstructor
public class GuichesController {

    private final GerenciarGuicheUseCase gerenciarGuicheUseCase;

    @GetMapping
    public ResponseEntity<List<GuicheResponseDto>> listar() {
        return ResponseEntity.ok(gerenciarGuicheUseCase.listarAtivos());
    }

    @PostMapping
    public ResponseEntity<GuicheResponseDto> criar(@Valid @RequestBody GuicheRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gerenciarGuicheUseCase.criar(request));
    }
}
