package com.viacao.calango.api.application.usecase;

import com.viacao.calango.api.application.dto.PassagemRequestDto;
import com.viacao.calango.api.domain.entity.Parada;
import com.viacao.calango.api.domain.entity.Passagem;
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

        // 1. Calcula o Preço Dinâmico
        BigDecimal precoFinal = calculadoraPrecoService.calcularPrecoFinal(
                request.precoBaseRota(),
                viagem,
                LocalDateTime.now(),
                request.isTrajetoCompleto()
        );

        // 2. Aloca o melhor assento (Lógica do Pinga-Pinga desenvolvida anteriormente)
        Integer assento = alocacaoPassageiroUseCase.alocarMelhorAssento(viagem.getId());

        // 3. Emite a Passagem
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