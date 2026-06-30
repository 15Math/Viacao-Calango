package com.viacao.calango.api.application.usecase;

import com.viacao.calango.api.application.dto.AlocarMotoristaRequestDto;
import com.viacao.calango.api.application.dto.MotoristaResponseDto;
import com.viacao.calango.api.domain.entity.EscalaMotorista;
import com.viacao.calango.api.domain.entity.Motorista;
import com.viacao.calango.api.domain.entity.Parada;
import com.viacao.calango.api.domain.entity.Viagem;
import com.viacao.calango.api.domain.enums.StatusMotorista;
import com.viacao.calango.api.domain.exception.MotoristaIndisponivelException;
import com.viacao.calango.api.domain.exception.RegraNegocioException;
import com.viacao.calango.api.infrastructure.repository.EscalaMotoristaRepository;
import com.viacao.calango.api.infrastructure.repository.MotoristaRepository;
import com.viacao.calango.api.infrastructure.repository.ParadaRepository;
import com.viacao.calango.api.infrastructure.repository.ViagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AlocarMotoristaUseCase {

    private final MotoristaRepository motoristaRepository;
    private final ViagemRepository viagemRepository;
    private final ParadaRepository paradaRepository;
    private final EscalaMotoristaRepository escalaMotoristaRepository;

    private static final int HORAS_DESCANSO = 12;
    private static final double LIMITE_HORAS = 6.0;
    private static final double LIMITE_KM = 400.0;

    @Transactional
    public MotoristaResponseDto executar(AlocarMotoristaRequestDto request) {
        Motorista motorista = motoristaRepository.findById(request.motoristaId())
                .orElseThrow(() -> new RegraNegocioException("Motorista não encontrado."));

        Viagem viagem = viagemRepository.findById(request.viagemId())
                .orElseThrow(() -> new RegraNegocioException("Viagem não encontrada."));

        Parada inicio = paradaRepository.findById(request.paradaInicioId())
                .orElseThrow(() -> new RegraNegocioException("Parada inicial inválida."));

        Parada fim = paradaRepository.findById(request.paradaFimId())
                .orElseThrow(() -> new RegraNegocioException("Parada final inválida."));

        // LÓGICA DE RESET DE TURNO: Se cumpriu as 12h de descanso, zera os acumuladores diários
        if (motorista.getFimUltimoTurno() != null) {
            long horasDescansadas = ChronoUnit.HOURS.between(motorista.getFimUltimoTurno(), LocalDateTime.now());
            if (horasDescansadas >= HORAS_DESCANSO && motorista.getStatus() == StatusMotorista.EM_DESCANSO) {
                motorista.setStatus(StatusMotorista.DISPONIVEL);
                motorista.setHorasDirigidasHoje(0.0);
                motorista.setKmDirigidosHoje(0.0);
            }
        }

        if (!isAptoParaDirigir(motorista, request.duracaoEstimadaTrecho(), request.kmTrecho())) {
            throw new MotoristaIndisponivelException("O motorista selecionado viola as regras de descanso ou limites diários de direção.");
        }

        EscalaMotorista escala = new EscalaMotorista();
        escala.setViagem(viagem);
        escala.setMotorista(motorista);
        escala.setParadaInicio(inicio);
        escala.setParadaFim(fim);

        motorista.setStatus(StatusMotorista.EM_VIAGEM);
        motorista.setHorasDirigidasHoje(motorista.getHorasDirigidasHoje() + request.duracaoEstimadaTrecho());
        motorista.setKmDirigidosHoje(motorista.getKmDirigidosHoje() + request.kmTrecho());

        escalaMotoristaRepository.save(escala);
        motoristaRepository.save(motorista);

        return MotoristaResponseDto.fromEntity(motorista);
    }

    // Método para auxiliar a listagem de motoristas prontos para escala denovo
    @Transactional(readOnly = true)
    public List<Motorista> listarMotoristasDisponiveis() {
        return motoristaRepository.findAll().stream()
                .filter(m -> m.getStatus() == StatusMotorista.DISPONIVEL ||
                        (m.getFimUltimoTurno() != null && ChronoUnit.HOURS.between(m.getFimUltimoTurno(), LocalDateTime.now()) >= HORAS_DESCANSO))
                .toList();
    }

    private boolean isAptoParaDirigir(Motorista motorista, Double duracaoTrecho, Double kmTrecho) {
        if (motorista.getStatus() == StatusMotorista.EM_DESCANSO) {
            return false;
        }
        if (motorista.getFimUltimoTurno() != null) {
            long horas = ChronoUnit.HOURS.between(motorista.getFimUltimoTurno(), LocalDateTime.now());
            if (horas < HORAS_DESCANSO) return false;
        }
        if (motorista.getHorasDirigidasHoje() + duracaoTrecho > LIMITE_HORAS) return false;
        if (motorista.getKmDirigidosHoje() + kmTrecho > LIMITE_KM) return false;
        return true;
    }
}