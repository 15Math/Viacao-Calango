package com.viacao.calango.api.application.usecase;

import com.viacao.calango.api.application.dto.AlocarMotoristaRequestDto;
import com.viacao.calango.api.application.dto.EscalaMotoristaResponseDto;
import com.viacao.calango.api.domain.entity.Motorista;
import com.viacao.calango.api.domain.entity.RotaParada;
import com.viacao.calango.api.domain.entity.Viagem;
import com.viacao.calango.api.domain.exception.RegraNegocioException;
import com.viacao.calango.api.infrastructure.repository.EscalaMotoristaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlocarMotoristasAutomaticoUseCase {

    private final GerenciarViagemUseCase gerenciarViagemUseCase;
    private final AlocarMotoristaUseCase alocarMotoristaUseCase;
    private final EscalaMotoristaRepository escalaMotoristaRepository;

    @Value("${calango.operacao.velocidade-media-kmh:80}")
    private double velocidadeMediaKmh;

    private static final double LIMITE_HORAS = 6.0;
    private static final double LIMITE_KM = 400.0;

    @Transactional
    public List<EscalaMotoristaResponseDto> executar(Long viagemId) {
        Viagem viagem = gerenciarViagemUseCase.buscarEntidade(viagemId);
        List<RotaParada> itinerario = viagem.getRota().getItinerario();

        if (itinerario.size() < 2) {
            throw new RegraNegocioException("A rota precisa de ao menos duas paradas para alocação de motoristas.");
        }

        if (!escalaMotoristaRepository.findByViagemId(viagemId).isEmpty()) {
            throw new RegraNegocioException("Esta viagem já possui motoristas alocados. Cancele as escalas antes de realocar.");
        }

        List<EscalaMotoristaResponseDto> escalas = new ArrayList<>();
        int inicioTrecho = 0;
        double kmAcumulado = 0;
        double horasAcumuladas = 0;

        for (int i = 0; i < itinerario.size() - 1; i++) {
            Double dist = itinerario.get(i).getDistanciaProximaParadaKm();
            double kmSegmento = dist != null ? dist : 0;
            double horasSegmento = kmSegmento / velocidadeMediaKmh;

            boolean ultimoSegmento = (i == itinerario.size() - 2);
            boolean paradaTroca = Boolean.TRUE.equals(itinerario.get(i + 1).getParadaTrocaMotorista());
            boolean excedeLimites = (kmAcumulado + kmSegmento > LIMITE_KM) || (horasAcumuladas + horasSegmento > LIMITE_HORAS);

            if (ultimoSegmento || paradaTroca || excedeLimites) {
                kmAcumulado += kmSegmento;
                horasAcumuladas += horasSegmento;

                Motorista motorista = selecionarMotorista(kmAcumulado, horasAcumuladas);
                var request = new AlocarMotoristaRequestDto(
                        viagemId,
                        motorista.getId(),
                        itinerario.get(inicioTrecho).getParada().getId(),
                        itinerario.get(i + 1).getParada().getId(),
                        horasAcumuladas,
                        kmAcumulado
                );
                alocarMotoristaUseCase.executar(request);

                escalas.add(new EscalaMotoristaResponseDto(
                        null, motorista.getId(), motorista.getNome(),
                        request.paradaInicioId(), itinerario.get(inicioTrecho).getParada().getNome(),
                        request.paradaFimId(), itinerario.get(i + 1).getParada().getNome()
                ));

                inicioTrecho = i + 1;
                kmAcumulado = 0;
                horasAcumuladas = 0;
            } else {
                kmAcumulado += kmSegmento;
                horasAcumuladas += horasSegmento;
            }
        }

        return escalaMotoristaRepository.findByViagemId(viagemId).stream()
                .map(EscalaMotoristaResponseDto::fromEntity)
                .toList();
    }

    private Motorista selecionarMotorista(double kmTrecho, double horasTrecho) {
        return alocarMotoristaUseCase.listarMotoristasDisponiveis().stream()
                .filter(m -> alocarMotoristaUseCase.isAptoParaTrecho(m, horasTrecho, kmTrecho))
                .findFirst()
                .orElseThrow(() -> new RegraNegocioException(
                        "Não há motorista disponível para o trecho de " + String.format("%.1f", kmTrecho)
                                + " km / " + String.format("%.1f", horasTrecho) + " h."
                ));
    }
}
