package com.viacao.calango.api.application.usecase;

import com.viacao.calango.api.domain.entity.OcupacaoAssento;
import com.viacao.calango.api.domain.exception.RegraNegocioException;
import com.viacao.calango.api.infrastructure.repository.OcupacaoAssentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlocacaoPassageiroUseCase {

    private final OcupacaoAssentoRepository ocupacaoRepository;

    @Transactional
    public Integer alocarMelhorAssento(Long viagemId, Integer ordemInicio, Integer ordemFim) {
        long totalSegmentosRequeridos = ordemFim - ordemInicio;

        List<Integer> assentosLivres = ocupacaoRepository.findAssentosDisponiveisNoTrecho(
                viagemId, ordemInicio, ordemFim, totalSegmentosRequeridos
        );

        if (assentosLivres == null || assentosLivres.isEmpty()) {
            throw new RegraNegocioException("Não há assentos disponíveis para esta viagem no trecho selecionado.");
        }

        Integer assentoSelecionado = assentosLivres.get(0);

        List<OcupacaoAssento> ocupacoes = ocupacaoRepository.findByViagemAndAssento(viagemId, assentoSelecionado);
        for (OcupacaoAssento ocupacao : ocupacoes) {
            if (ocupacao.getOrdemSegmento() >= ordemInicio && ocupacao.getOrdemSegmento() < ordemFim) {
                ocupacao.setStatus("OCUPADO");
            }
        }
        ocupacaoRepository.saveAll(ocupacoes);

        return assentoSelecionado;
    }
}