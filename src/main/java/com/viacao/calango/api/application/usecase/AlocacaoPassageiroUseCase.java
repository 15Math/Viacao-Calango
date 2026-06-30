package com.viacao.calango.api.application.usecase;

import com.viacao.calango.api.domain.exception.RegraNegocioException;
import com.viacao.calango.api.infrastructure.repository.OcupacaoAssentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlocacaoPassageiroUseCase {

    private final OcupacaoAssentoRepository ocupacaoRepository;

    public Integer alocarMelhorAssento(Long viagemId) {
        // Busca os assentos disponíveis no banco
        List<Integer> assentosLivres = ocupacaoRepository.findAssentosDisponiveis(viagemId);

        if (assentosLivres == null || assentosLivres.isEmpty()) {
            throw new RegraNegocioException("Não há assentos disponíveis para esta viagem.");
        }
        return assentosLivres.get(0);
    }
}