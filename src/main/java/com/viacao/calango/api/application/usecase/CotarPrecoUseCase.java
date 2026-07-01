package com.viacao.calango.api.application.usecase;

import com.viacao.calango.api.application.dto.CotacaoResponseDto;
import com.viacao.calango.api.domain.entity.Parada;
import com.viacao.calango.api.domain.entity.RotaParada;
import com.viacao.calango.api.domain.entity.Viagem;
import com.viacao.calango.api.domain.enums.StatusViagem;
import com.viacao.calango.api.domain.exception.RegraNegocioException;
import com.viacao.calango.api.domain.service.CalculadoraPrecoService;
import com.viacao.calango.api.domain.service.RotaUtilService;
import com.viacao.calango.api.infrastructure.config.PricingConfig;
import com.viacao.calango.api.infrastructure.repository.ParadaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CotarPrecoUseCase {

    private final GerenciarViagemUseCase gerenciarViagemUseCase;
    private final ParadaRepository paradaRepository;
    private final CalculadoraPrecoService calculadoraPrecoService;
    private final RotaUtilService rotaUtilService;
    private final PricingConfig pricingConfig;

    @Transactional(readOnly = true)
    public CotacaoResponseDto cotar(Long viagemId, Long origemId, Long destinoId) {
        Viagem viagem = gerenciarViagemUseCase.buscarEntidade(viagemId);
        if (viagem.getStatus() != StatusViagem.PROGRAMADA) {
            throw new RegraNegocioException("Esta viagem não está disponível para venda.");
        }
        if (viagem.getOnibus().precisaRevisao()) {
            throw new RegraNegocioException("O ônibus desta viagem está bloqueado para revisão.");
        }

        Parada origem = paradaRepository.findById(origemId)
                .orElseThrow(() -> new RegraNegocioException("Origem inválida."));
        Parada destino = paradaRepository.findById(destinoId)
                .orElseThrow(() -> new RegraNegocioException("Destino inválido."));

        List<RotaParada> itinerario = viagem.getRota().getItinerario();
        int[] ordens = rotaUtilService.resolverOrdensTrecho(itinerario, origemId, destinoId);
        boolean trajetoCompleto = rotaUtilService.isTrajetoCompleto(itinerario, origemId, destinoId);

        BigDecimal precoTrecho = calculadoraPrecoService.calcularPrecoTrecho(viagem.getRota(), ordens[0], ordens[1]);
        LocalDateTime agora = LocalDateTime.now();
        BigDecimal precoFinal = calculadoraPrecoService.calcularPrecoFinal(
                precoTrecho, agora, viagem, viagem.getOnibus().getTipo().name(), trajetoCompleto
        );

        List<String> descontos = new ArrayList<>();
        long dias = ChronoUnit.DAYS.between(agora.toLocalDate(), viagem.getDataHoraSaida().toLocalDate());
        pricingConfig.descontoParaDiasAntecedencia(dias)
                .ifPresent(d -> descontos.add("Antecedência (" + dias + " dias): " + (int) (d * 100) + "%"));
        if (trajetoCompleto && pricingConfig.getDescontoTrajetoCompleto() != null) {
            descontos.add("Trajeto completo: " + (int) (pricingConfig.getDescontoTrajetoCompleto() * 100) + "%");
        }
        descontos.add("Tipo de ônibus: " + viagem.getOnibus().getTipo());

        return new CotacaoResponseDto(
                viagemId, origemId, origem.getNome(), destinoId, destino.getNome(),
                precoTrecho, precoFinal, trajetoCompleto, descontos
        );
    }
}
