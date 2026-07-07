package com.viacao.calango.api.application.usecase;

import com.viacao.calango.api.application.dto.ParadaRequestDto;
import com.viacao.calango.api.application.dto.ParadaResponseDto;
import com.viacao.calango.api.domain.entity.Parada;
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

    @Transactional
    public ParadaResponseDto criar(ParadaRequestDto request) {
        Parada novaParada = new Parada();
        novaParada.setNome(request.nome());
        novaParada.setCidade(request.cidade());
        novaParada.setEstado(request.estado());

        Parada paradaSalva = paradaRepository.save(novaParada);

        return ParadaResponseDto.fromEntity(paradaSalva);
    }
}
