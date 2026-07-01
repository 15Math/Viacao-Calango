package com.viacao.calango.api.presentation.controller;

import com.viacao.calango.api.application.dto.*;
import com.viacao.calango.api.application.usecase.*;
import com.viacao.calango.api.domain.enums.StatusViagem;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/viagens")
@RequiredArgsConstructor
public class ViagensController {

    private final GerenciarViagemUseCase gerenciarViagemUseCase;
    private final ConsultarAssentosUseCase consultarAssentosUseCase;
    private final CotarPrecoUseCase cotarPrecoUseCase;
    private final OtimizarAlocacaoPassageirosUseCase otimizarAlocacaoPassageirosUseCase;

    @GetMapping
    public ResponseEntity<List<ViagemResumoDto>> buscar(
            @RequestParam(required = false) Long origemId,
            @RequestParam(required = false) Long destinoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) Long rotaId
    ) {
        return ResponseEntity.ok(gerenciarViagemUseCase.buscar(origemId, destinoId, data, rotaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ViagemDetalheDto> buscarDetalhe(@PathVariable Long id) {
        return ResponseEntity.ok(gerenciarViagemUseCase.buscarDetalhe(id));
    }

    @PostMapping
    public ResponseEntity<ViagemDetalheDto> criar(@Valid @RequestBody CriarViagemRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gerenciarViagemUseCase.criar(request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ViagemDetalheDto> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusViagem status
    ) {
        return ResponseEntity.ok(gerenciarViagemUseCase.atualizarStatus(id, status));
    }

    @GetMapping("/{id}/assentos")
    public ResponseEntity<List<AssentoMapaDto>> consultarAssentos(
            @PathVariable Long id,
            @RequestParam Long origemId,
            @RequestParam Long destinoId
    ) {
        return ResponseEntity.ok(consultarAssentosUseCase.consultarMapa(id, origemId, destinoId));
    }

    @GetMapping("/{id}/cotacao")
    public ResponseEntity<CotacaoResponseDto> cotar(
            @PathVariable Long id,
            @RequestParam Long origemId,
            @RequestParam Long destinoId
    ) {
        return ResponseEntity.ok(cotarPrecoUseCase.cotar(id, origemId, destinoId));
    }

    @GetMapping("/otimizacao")
    public ResponseEntity<Map<String, Object>> otimizarAlocacao(
            @RequestParam Long rotaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data
    ) {
        return ResponseEntity.ok(otimizarAlocacaoPassageirosUseCase.analisar(rotaId, data));
    }
}
