package com.viacao.calango.api.presentation.controller;

import com.viacao.calango.api.application.dto.OnibusRequestDto;
import com.viacao.calango.api.application.dto.OnibusResponseDto;
import com.viacao.calango.api.application.usecase.ControlarRevisaoUseCase;
import com.viacao.calango.api.application.usecase.GerenciarOnibusUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/frota")
@RequiredArgsConstructor
public class FrotaController {

    private final GerenciarOnibusUseCase gerenciarOnibusUseCase;
    private final ControlarRevisaoUseCase controlarRevisaoUseCase;

    //cadastra um onibus
    @PostMapping("/onibus")
    public ResponseEntity<OnibusResponseDto> cadastrarOnibus(@RequestBody OnibusRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(gerenciarOnibusUseCase.cadastrar(request));
    }

    //Lista geral de onibua
    @GetMapping("/onibus")
    public ResponseEntity<List<OnibusResponseDto>> listarOnibus() {
        return ResponseEntity.ok(gerenciarOnibusUseCase.listar());
    }

    //Lista dos onibus dispoinveis
    @GetMapping("/onibus/disponiveis")
    public ResponseEntity<List<OnibusResponseDto>> listarDisponiveis() {
        return ResponseEntity.ok(gerenciarOnibusUseCase.listarDisponiveis());
    }

    //Lista dos onibus pendentes
    @GetMapping("/onibus/revisao-pendente")
    public ResponseEntity<List<OnibusResponseDto>> listarRevisaoPendente() {
        return ResponseEntity.ok(gerenciarOnibusUseCase.listarRevisaoPendente());
    }

    //Seta o fim da viagem e verifica km rodado
    @PostMapping("/{onibusId}/fim-viagem")
    public ResponseEntity<Void> registrarFimViagem(@PathVariable Long onibusId, @RequestParam Double kmRodados) {
        controlarRevisaoUseCase.registrarFimViagem(onibusId, kmRodados);
        return ResponseEntity.ok().build();
    }

    //marca a revisao como concluida
    @PostMapping("/{onibusId}/concluir-revisao")
    public ResponseEntity<Void> concluirRevisao(@PathVariable Long onibusId) {
        controlarRevisaoUseCase.realizarRevisao(onibusId);
        return ResponseEntity.ok().build();
    }
}
