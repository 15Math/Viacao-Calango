package com.viacao.calango.api.application.usecase;

import com.viacao.calango.api.application.dto.PassagemRequestDto;
import com.viacao.calango.api.domain.entity.Parada;
import com.viacao.calango.api.domain.entity.Passagem;
import com.viacao.calango.api.domain.entity.RotaParada;
import com.viacao.calango.api.domain.entity.Viagem;
import com.viacao.calango.api.domain.exception.RegraNegocioException;
import com.viacao.calango.api.domain.service.CalculadoraPrecoService;
import com.viacao.calango.api.infrastructure.repository.ParadaRepository;
import com.viacao.calango.api.infrastructure.repository.PassagemRepository;
import com.viacao.calango.api.infrastructure.repository.ViagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VenderPassagemUseCase {

    private final ViagemRepository viagemRepository;
    private final ParadaRepository paradaRepository;
    private final PassagemRepository passagemRepository;
    private final CalculadoraPrecoService calculadoraPrecoService;
    private final AlocacaoPassageiroUseCase alocacaoPassageiroUseCase;

    @Transactional
    public Passagem vender(PassagemRequestDto request) {
        Viagem viagem = viagemRepository.findById(request.viagemId())
                .orElseThrow(() -> new RegraNegocioException("Viagem não encontrada."));
        Parada origem = paradaRepository.findById(request.origemId())
                .orElseThrow(() -> new RegraNegocioException("Origem inválida."));
        Parada destino = paradaRepository.findById(request.destinoId())
                .orElseThrow(() -> new RegraNegocioException("Destino inválido."));

        List<RotaParada> itinerario = viagem.getRota().getItinerario();
        if (itinerario == null || itinerario.isEmpty()) {
            throw new RegraNegocioException("A rota desta viagem não possui itinerário cadastrado.");
        }

        int ordemInicio = -1;
        int ordemFim = -1;

        for (int i = 0; i < itinerario.size(); i++) {
            if (itinerario.get(i).getParada().getId().equals(origem.getId())) {
                ordemInicio = i;
            }
            if (itinerario.get(i).getParada().getId().equals(destino.getId())) {
                ordemFim = i;
            }
        }

        if (ordemInicio == -1 || ordemFim == -1 || ordemInicio >= ordemFim) {
            throw new RegraNegocioException("O trecho selecionado não faz parte do itinerário ou está na ordem incorreta.");
        }

        Parada primeiraParada = itinerario.get(0).getParada();
        Parada ultimaParada = itinerario.get(itinerario.size() - 1).getParada();
        boolean isTrajetoCompleto = origem.getId().equals(primeiraParada.getId()) && destino.getId().equals(ultimaParada.getId());

        BigDecimal precoFinal = calculadoraPrecoService.calcularPrecoFinal(
                viagem.getRota().getPrecoBase(),
                LocalDateTime.now(),
                viagem,
                viagem.getOnibus().getTipo().name(),
                isTrajetoCompleto
        );

        Integer assento = alocacaoPassageiroUseCase.alocarMelhorAssento(viagem.getId(), ordemInicio, ordemFim);

        Passagem passagem = new Passagem();
        passagem.setViagem(viagem);
        passagem.setOrigem(origem);
        passagem.setDestino(destino);
        passagem.setNumeroAssento(assento);
        passagem.setValorPago(precoFinal);
        passagem.setDataCompra(LocalDateTime.now());
        passagem.setTipoPagamento(request.tipoPagamento());

        return passagemRepository.save(passagem);
    }
}