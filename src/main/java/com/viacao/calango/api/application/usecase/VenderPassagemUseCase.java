package com.viacao.calango.api.application.usecase;

import com.viacao.calango.api.application.dto.PassagemRequestDto;
import com.viacao.calango.api.domain.entity.*;
import com.viacao.calango.api.domain.enums.StatusPagamento;
import com.viacao.calango.api.domain.enums.StatusViagem;
import com.viacao.calango.api.domain.enums.TipoPagamento;
import com.viacao.calango.api.domain.exception.RegraNegocioException;
import com.viacao.calango.api.domain.port.GatewayPagamento;
import com.viacao.calango.api.domain.service.CalculadoraPrecoService;
import com.viacao.calango.api.domain.service.RotaUtilService;
import com.viacao.calango.api.infrastructure.repository.ParadaRepository;
import com.viacao.calango.api.infrastructure.repository.PassagemRepository;
import com.viacao.calango.api.infrastructure.repository.ViagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VenderPassagemUseCase {

    private final ViagemRepository viagemRepository;
    private final ParadaRepository paradaRepository;
    private final PassagemRepository passagemRepository;
    private final CalculadoraPrecoService calculadoraPrecoService;
    private final AlocacaoPassageiroUseCase alocacaoPassageiroUseCase;
    private final RotaUtilService rotaUtilService;
    private final GatewayPagamento gatewayPagamento;

    @Transactional
    public Passagem vender(PassagemRequestDto request) {
        Viagem viagem = viagemRepository.findDetalhadaById(request.viagemId())
                .orElseThrow(() -> new RegraNegocioException("Viagem não encontrada."));

        if (viagem.getStatus() != StatusViagem.PROGRAMADA) {
            throw new RegraNegocioException("Esta viagem não está disponível para venda.");
        }
        if (viagem.getOnibus().precisaRevisao()) {
            throw new RegraNegocioException("O ônibus desta viagem está bloqueado para revisão.");
        }

        Parada origem = paradaRepository.findById(request.origemId())
                .orElseThrow(() -> new RegraNegocioException("Origem inválida."));
        Parada destino = paradaRepository.findById(request.destinoId())
                .orElseThrow(() -> new RegraNegocioException("Destino inválido."));

        List<RotaParada> itinerario = viagem.getRota().getItinerario();
        if (itinerario == null || itinerario.isEmpty()) {
            throw new RegraNegocioException("A rota desta viagem não possui itinerário cadastrado.");
        }

        int[] ordens = rotaUtilService.resolverOrdensTrecho(itinerario, origem.getId(), destino.getId());
        boolean isTrajetoCompleto = rotaUtilService.isTrajetoCompleto(itinerario, origem.getId(), destino.getId());

        BigDecimal precoTrecho = calculadoraPrecoService.calcularPrecoTrecho(viagem.getRota(), ordens[0], ordens[1]);
        BigDecimal precoFinal = calculadoraPrecoService.calcularPrecoFinal(
                precoTrecho,
                LocalDateTime.now(),
                viagem,
                viagem.getOnibus().getTipo(),
                isTrajetoCompleto
        );

        boolean sucessoPagamento = gatewayPagamento.processar(precoFinal, request.tipoPagamento());
        if (!sucessoPagamento) {
            throw new RegraNegocioException("Falha na autorização do pagamento. Venda cancelada.");
        }

        Integer assento = request.numeroAssento() != null
                ? alocacaoPassageiroUseCase.alocarAssentoEspecifico(viagem.getId(), ordens[0], ordens[1], request.numeroAssento())
                : alocacaoPassageiroUseCase.alocarMelhorAssento(viagem.getId(), ordens[0], ordens[1]);

        Passagem passagem = new Passagem();
        passagem.setViagem(viagem);
        passagem.setOrigem(origem);
        passagem.setDestino(destino);
        passagem.setNumeroAssento(assento);
        passagem.setValorPago(precoFinal);
        passagem.setDataCompra(LocalDateTime.now());
        passagem.setTipoPagamento(request.tipoPagamento());
        passagem.setCodigoTransacao(gerarCodigoTransacao(request.tipoPagamento()));
        passagem.setStatusPagamento(StatusPagamento.PAGO);

        return passagemRepository.save(passagem);
    }

    private String gerarCodigoTransacao(TipoPagamento tipo) {
        String prefixo = tipo == TipoPagamento.CARTAO_INTERNET ? "WEB" : "TKT";
        return prefixo + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}