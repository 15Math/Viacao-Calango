package com.viacao.calango.api.application.usecase;

import com.viacao.calango.api.application.dto.PassagemResponseDto;
import com.viacao.calango.api.domain.entity.Passagem;
import com.viacao.calango.api.domain.exception.RegraNegocioException;
import com.viacao.calango.api.infrastructure.repository.PassagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConsultarPassagemUseCase {

    private final PassagemRepository passagemRepository;

    @Transactional(readOnly = true)
    public PassagemResponseDto buscar(Long id) {
        Passagem passagem = passagemRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Passagem não encontrada."));
        return PassagemResponseDto.fromEntity(passagem);
    }
}
