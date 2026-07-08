package com.viacao.calango.api.application.usecase;

import com.viacao.calango.api.domain.entity.*;
import com.viacao.calango.api.domain.enums.StatusAssento;
import com.viacao.calango.api.domain.exception.RegraNegocioException;
import com.viacao.calango.api.infrastructure.repository.ConfiguracaoSistemaRepository;
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
    private final ConfiguracaoSistemaRepository configuracaoRepository;

    private static final double LIMITE_REVISAO_KM_PADRAO = 10000.0;

    @Transactional
    public Viagem executar(Viagem viagem) {

        // busca configuração de limite de KM para revisão a partir do banco de dados
        double limiteRevisaoKm = configuracaoRepository.findByChave("LIMITE_REVISAO_KM")
                .map(config -> Double.parseDouble(config.getValor()))
                .orElse(LIMITE_REVISAO_KM_PADRAO);

        // impede a viagem se o onibus precisar de manutenção urgente
        if (viagem.getOnibus().getQuilometragemDesdeUltimaRevisao() >= limiteRevisaoKm) {
            throw new RegraNegocioException("Não é possível inicializar a viagem. O ônibus de placa "
                    + viagem.getOnibus().getPlaca() + " atingiu o limite de quilometragem e deve ser retido para revisão imediata!");
        }

        Viagem viagemSalva = viagemRepository.save(viagem);

        List<RotaParada> itinerario = viagemSalva.getRota().getItinerario();
        List<OcupacaoAssento> matrizOcupacao = new ArrayList<>();

        // itera pelos segmentos da rota para gerar a ocupação base
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
                ocupacao.setStatus(StatusAssento.LIVRE);
                viagemSalva.getOcupacoes().add(ocupacao);
                matrizOcupacao.add(ocupacao);
            }
        }

        ocupacaoRepository.saveAll(matrizOcupacao);

        return viagemSalva;
    }
}