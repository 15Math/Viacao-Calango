package com.viacao.calango.api.application.usecase;

import com.viacao.calango.api.application.dto.ParadaRequestDto;
import com.viacao.calango.api.application.dto.ParadaResponseDto;
import com.viacao.calango.api.domain.entity.Parada;
import com.viacao.calango.api.domain.exception.RegraNegocioException;
import com.viacao.calango.api.infrastructure.repository.ParadaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GerenciarParadaUseCase {

    private final ParadaRepository paradaRepository;

    @Transactional(readOnly = true)
    public List<ParadaResponseDto> listar() {
        return paradaRepository.findAll().stream().map(ParadaResponseDto::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public ParadaResponseDto buscar(Long id) {
        return ParadaResponseDto.fromEntity(buscarEntidade(id));
    }

    @Transactional
    public ParadaResponseDto criar(ParadaRequestDto request) {
        Parada parada = new Parada();
        parada.setNome(request.nome());
        parada.setCidade(request.cidade());
        parada.setEstado(request.estado());
        return ParadaResponseDto.fromEntity(paradaRepository.save(parada));
    }

    public Parada buscarEntidade(Long id) {
        return paradaRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Parada não encontrada."));
    }
}
