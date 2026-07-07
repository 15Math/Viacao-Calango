package com.viacao.calango.api.application.usecase;

import com.viacao.calango.api.domain.entity.EscalaMotorista;
import com.viacao.calango.api.domain.entity.Motorista;
import com.viacao.calango.api.domain.entity.Parada;
import com.viacao.calango.api.domain.entity.RotaParada;
import com.viacao.calango.api.domain.entity.Viagem;
import com.viacao.calango.api.domain.enums.StatusMotorista;
import com.viacao.calango.api.domain.exception.RegraNegocioException;
import com.viacao.calango.api.infrastructure.repository.MotoristaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlocarMotoristaUseCase {

    private final MotoristaRepository motoristaRepository;

    private static final double VELOCIDADE_MEDIA_KMH = 70.0;
    private static final double LIMITE_KM_TURNO = 400.0;
    private static final double LIMITE_HORAS_TURNO = 6.0;

    public void alocarMotoristasNaViagem(Viagem viagem, List<RotaParada> itinerario) {
        // Motorista precisa de 12 horas de descanso
        LocalDateTime limiteDescanso = viagem.getDataHoraSaida().minusHours(12);
        List<Motorista> motoristasDisponiveis = motoristaRepository.findDisponiveisComDescanso(StatusMotorista.DISPONIVEL, limiteDescanso);

        if (motoristasDisponiveis.isEmpty()) {
            throw new RegraNegocioException("Não há motoristas disponíveis com o descanso obrigatório de 12 horas.");
        }

        int indexMotorista = 0;
        double kmAcumuladoTrecho = 0.0;
        double horasAcumuladasTrecho = 0.0;

        Parada paradaInicioTrecho = itinerario.get(0).getParada();

        // Percorre o itinerário trecho a trecho (parada a parada)
        for (int i = 0; i < itinerario.size() - 1; i++) {
            RotaParada paradaOrigem = itinerario.get(i);
            RotaParada paradaDestino = itinerario.get(i + 1);
            // Calcula distância e tempo baseados na velocidade média do sistema
            double distancia = paradaOrigem.getDistanciaProximaParadaKm() != null ? paradaOrigem.getDistanciaProximaParadaKm() : 0.0;
            kmAcumuladoTrecho += distancia;
            horasAcumuladasTrecho += (distancia / VELOCIDADE_MEDIA_KMH);

            if (kmAcumuladoTrecho > LIMITE_KM_TURNO || horasAcumuladasTrecho > LIMITE_HORAS_TURNO) {
                throw new RegraNegocioException(String.format(
                        "O itinerário exige direção acima de 400km ou 6h sem ponto de troca (Entre %s e %s). Ajuste a rota.",
                        paradaInicioTrecho.getNome(), paradaDestino.getParada().getNome()
                ));
            }

            boolean isFimDaViagem = (i + 1 == itinerario.size() - 1);
            boolean isPontoDeTroca = paradaDestino.getParadaTrocaMotorista() != null && paradaDestino.getParadaTrocaMotorista();

            // Momento de trocar de motorista ou finalizar a escala
            if (isPontoDeTroca || isFimDaViagem) {
                if (indexMotorista >= motoristasDisponiveis.size()) {
                    throw new RegraNegocioException("Não há motoristas suficientes com 12h de descanso para cobrir todo o trajeto.");
                }

                Motorista motoristaAlocado = motoristasDisponiveis.get(indexMotorista++);

                EscalaMotorista escala = new EscalaMotorista();
                escala.setViagem(viagem);
                escala.setMotorista(motoristaAlocado);
                escala.setParadaInicio(paradaInicioTrecho);
                escala.setParadaFim(paradaDestino.getParada());

                viagem.getEscalas().add(escala);

                // Atualiza o início do próximo trecho e zera acumuladores
                paradaInicioTrecho = paradaDestino.getParada();
                kmAcumuladoTrecho = 0.0;
                horasAcumuladasTrecho = 0.0;
            }
        }
    }
}