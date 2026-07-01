package com.viacao.calango.api.application.usecase;

import com.viacao.calango.api.application.dto.AssentoMapaDto;
import com.viacao.calango.api.domain.entity.OcupacaoAssento;
import com.viacao.calango.api.domain.entity.RotaParada;
import com.viacao.calango.api.domain.entity.Viagem;
import com.viacao.calango.api.domain.service.RotaUtilService;
import com.viacao.calango.api.infrastructure.repository.OcupacaoAssentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultarAssentosUseCase {

    private final GerenciarViagemUseCase gerenciarViagemUseCase;
    private final OcupacaoAssentoRepository ocupacaoRepository;
    private final RotaUtilService rotaUtilService;

    @Transactional(readOnly = true)
    public List<AssentoMapaDto> consultarMapa(Long viagemId, Long origemId, Long destinoId) {
        Viagem viagem = gerenciarViagemUseCase.buscarEntidade(viagemId);
        List<RotaParada> itinerario = viagem.getRota().getItinerario();
        int[] ordens = rotaUtilService.resolverOrdensTrecho(itinerario, origemId, destinoId);
        int ordemInicio = ordens[0];
        int ordemFim = ordens[1];
        long totalSegmentos = ordemFim - ordemInicio;

        List<Integer> livres = ocupacaoRepository.findAssentosDisponiveisNoTrecho(
                viagemId, ordemInicio, ordemFim, totalSegmentos
        );

        List<AssentoMapaDto> mapa = new ArrayList<>();
        for (int assento = 1; assento <= viagem.getOnibus().getCapacidade(); assento++) {
            String status = livres.contains(assento) ? "LIVRE" : "OCUPADO";
            mapa.add(new AssentoMapaDto(assento, status));
        }
        return mapa;
    }
}
