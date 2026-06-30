package com.viacao.calango.api.application.usecase;

import com.viacao.calango.api.application.dto.AlocarMotoristaRequestDto;
import com.viacao.calango.api.application.dto.MotoristaResponseDto;
import com.viacao.calango.api.domain.entity.Motorista;
import com.viacao.calango.api.domain.enums.StatusMotorista;
import com.viacao.calango.api.domain.exception.MotoristaIndisponivelException;
import com.viacao.calango.api.infrastructure.repository.MotoristaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlocarMotoristaUseCase {

    private final MotoristaRepository motoristaRepository;

    private static final int HORAS_DESCANSO = 12;
    private static final double LIMITE_HORAS = 6.0;
    private static final double LIMITE_KM = 400.0;

    @Transactional
    public MotoristaResponseDto executar(AlocarMotoristaRequestDto request) {
        List<Motorista> disponiveis = motoristaRepository.findByStatus(StatusMotorista.DISPONIVEL);

        Motorista motoristaAlocado = disponiveis.stream()
                .filter(m -> isAptoParaDirigir(m, request.duracaoEstimadaTrecho(), request.kmTrecho()))
                .findFirst()
                .orElseThrow(() -> new MotoristaIndisponivelException("Nenhum motorista apto para os limites informados."));

        motoristaAlocado.setStatus(StatusMotorista.EM_VIAGEM);
        motoristaRepository.save(motoristaAlocado);

        return MotoristaResponseDto.fromEntity(motoristaAlocado);
    }

    private boolean isAptoParaDirigir(Motorista motorista, Double duracaoTrecho, Double kmTrecho) {
        if (motorista.getFimUltimoTurno() != null) {
            long horas = ChronoUnit.HOURS.between(motorista.getFimUltimoTurno(), LocalDateTime.now());
            if (horas < HORAS_DESCANSO) return false;
        }
        if (motorista.getHorasDirigidasHoje() + duracaoTrecho > LIMITE_HORAS) return false;
        if (motorista.getKmDirigidosHoje() + kmTrecho > LIMITE_KM) return false;
        return true;
    }
}