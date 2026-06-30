package com.viacao.calango.api.application.usecase;

import com.viacao.calango.api.domain.entity.*;
import com.viacao.calango.api.domain.exception.RegraNegocioException;
import com.viacao.calango.api.infrastructure.repository.OcupacaoAssentoRepository;
import com.viacao.calango.api.infrastructure.repository.ViagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InicializarViagemUseCase {

    private final ViagemRepository viagemRepository;
    private final OcupacaoAssentoRepository ocupacaoRepository;

    @Transactional
    public Viagem ejecutar(Viagem viagem) {
        // BLOQUEIO OPERACIONAL: Impede a viagem de iniciar se o ônibus estourou a quilometragem sem revisão
        if (viagem.getOnibus().getQuilometragemDesdeUltimaRevisao() >= 10000.0) {
            throw new RegraNegocioException("Não é possível inicializar a viagem. O ônibus de placa "
                    + viagem.getOnibus().getPlaca() + " atingiu o limite de quilometragem e deve ser retido para revisão imediata!");
        }

        Viagem viagemSalva = viagemRepository.save(viagem);
        List<RotaParada> itinerario = viagemSalva.getRota().getItinerario();
        List<OcupacaoAssento> matrizOcupacao = new ArrayList<>();

        for (int i = 0; i < itinerario.size() - 1; i++) {
            Parada origenSegmento = itinerario.get(i).getParada();
            Parada destinoSegmento = itinerario.get(i + 1).getParada();

            for (int assento = 1; assento <= viagemSalva.getOnibus().getCapacidade(); assento++) {
                OcupacaoAssento ocupacao = new OcupacaoAssento();
                ocupacao.setViagem(viagemSalva);
                ocupacao.setOrigemSegmento(origenSegmento);
                ocupacao.setDestinoSegmento(destinoSegmento);
                ocupacao.setNumeroAssento(assento);
                ocupacao.setOrdemSegmento(i);
                ocupacao.setStatus("LIVRE");
                matrizOcupacao.add(ocupacao);
            }
        }

        ocupacaoRepository.saveAll(matrizOcupacao);
        return viagemSalva;
    }
}