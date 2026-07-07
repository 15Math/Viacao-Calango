package com.viacao.calango.api.application.usecase;

import com.viacao.calango.api.domain.entity.OcupacaoAssento;
import com.viacao.calango.api.domain.enums.StatusAssento;
import com.viacao.calango.api.domain.exception.AssentoOcupadoException;
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
                viagemId, ordemInicio, ordemFim, totalSegmentosRequeridos,StatusAssento.LIVRE
        );

        if (assentosLivres == null || assentosLivres.isEmpty()) {
            throw new RegraNegocioException("Não há assentos disponíveis para esta viagem no trecho selecionado.");
        }

        return ocuparAssento(viagemId, ordemInicio, ordemFim, assentosLivres.get(0));
    }

    @Transactional
    public Integer alocarAssentoEspecifico(Long viagemId, Integer ordemInicio, Integer ordemFim, Integer numeroAssento) {
        long totalSegmentosRequeridos = ordemFim - ordemInicio;

        List<Integer> assentosLivres = ocupacaoRepository.findAssentosDisponiveisNoTrecho(
                viagemId, ordemInicio, ordemFim, totalSegmentosRequeridos, StatusAssento.LIVRE
        );

        if (!assentosLivres.contains(numeroAssento)) {
            throw new AssentoOcupadoException("O assento " + numeroAssento + " não está disponível para este trecho.");
        }

        return ocuparAssento(viagemId, ordemInicio, ordemFim, numeroAssento);
    }

    private Integer ocuparAssento(Long viagemId, Integer ordemInicio, Integer ordemFim, Integer assentoSelecionado) {
        List<OcupacaoAssento> ocupacoes = ocupacaoRepository.findByViagemAndAssento(viagemId, assentoSelecionado);
        for (OcupacaoAssento ocupacao : ocupacoes) {
            if (ocupacao.getOrdemSegmento() >= ordemInicio && ocupacao.getOrdemSegmento() < ordemFim) {
                ocupacao.setStatus(StatusAssento.OCUPADO);
            }
        }
        ocupacaoRepository.saveAll(ocupacoes);
        return assentoSelecionado;
    }
}
