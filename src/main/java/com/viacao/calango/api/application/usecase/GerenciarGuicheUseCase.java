package com.viacao.calango.api.application.usecase;

import com.viacao.calango.api.application.dto.GuicheRequestDto;
import com.viacao.calango.api.application.dto.GuicheResponseDto;
import com.viacao.calango.api.domain.entity.Guiche;
import com.viacao.calango.api.domain.entity.Parada;
import com.viacao.calango.api.infrastructure.repository.GuicheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GerenciarGuicheUseCase {

    private final GuicheRepository guicheRepository;
    private final GerenciarParadaUseCase gerenciarParadaUseCase;

    @Transactional(readOnly = true)
    public List<GuicheResponseDto> listarAtivos() {
        return guicheRepository.findByAtivoTrue().stream().map(GuicheResponseDto::fromEntity).toList();
    }

    @Transactional
    public GuicheResponseDto criar(GuicheRequestDto request) {
        Parada parada = gerenciarParadaUseCase.buscarEntidade(request.paradaId());
        Guiche guiche = new Guiche();
        guiche.setNome(request.nome());
        guiche.setCidade(request.cidade());
        guiche.setEstado(request.estado());
        guiche.setParada(parada);
        guiche.setAtivo(true);
        return GuicheResponseDto.fromEntity(guicheRepository.save(guiche));
    }
}
