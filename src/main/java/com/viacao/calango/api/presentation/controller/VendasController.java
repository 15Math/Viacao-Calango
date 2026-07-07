package com.viacao.calango.api.presentation.controller;

import com.viacao.calango.api.application.dto.PassagemRequestDto;
import com.viacao.calango.api.application.dto.PassagemResponseDto;
import com.viacao.calango.api.application.usecase.ConsultarPassagemUseCase;
import com.viacao.calango.api.application.usecase.VenderPassagemUseCase;
import com.viacao.calango.api.domain.entity.Passagem;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendas")
@RequiredArgsConstructor
public class VendasController {

    private final VenderPassagemUseCase venderPassagemUseCase;
    private final ConsultarPassagemUseCase consultarPassagemUseCase;

    //Compra da passagem
    @PostMapping
    public ResponseEntity<PassagemResponseDto> realizarVenda(@Valid @RequestBody PassagemRequestDto request) {
        Passagem novaPassagem = venderPassagemUseCase.vender(request);
        PassagemResponseDto response = PassagemResponseDto.fromEntity(novaPassagem);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //buscar informações da passagem
    @GetMapping("/passagens/{id}")
    public ResponseEntity<PassagemResponseDto> buscarPassagem(@PathVariable Long id) {
        return ResponseEntity.ok(consultarPassagemUseCase.buscar(id));
    }
}
