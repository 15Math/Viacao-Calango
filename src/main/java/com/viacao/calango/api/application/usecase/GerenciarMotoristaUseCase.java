package com.viacao.calango.api.application.usecase;

import com.viacao.calango.api.application.dto.MotoristaDetalheDto;
import com.viacao.calango.api.application.dto.MotoristaRequestDto;
import com.viacao.calango.api.domain.entity.Motorista;
import com.viacao.calango.api.domain.enums.StatusMotorista;
import com.viacao.calango.api.domain.exception.RegraNegocioException;
import com.viacao.calango.api.infrastructure.repository.MotoristaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GerenciarMotoristaUseCase {

    private final MotoristaRepository motoristaRepository;

    @Transactional(readOnly = true)
    public List<MotoristaDetalheDto> listar() {
        return motoristaRepository.findAll().stream().map(MotoristaDetalheDto::fromEntity).toList();
    }

    @Transactional
    public MotoristaDetalheDto criar(MotoristaRequestDto request) {
        Motorista motorista = new Motorista();
        motorista.setNome(request.nome());
        motorista.setCnh(request.cnh());
        motorista.setStatus(StatusMotorista.DISPONIVEL);
        motorista.setHorasDirigidasHoje(0.0);
        motorista.setKmDirigidosHoje(0.0);
        return MotoristaDetalheDto.fromEntity(motoristaRepository.save(motorista));
    }

    @Transactional
    public MotoristaDetalheDto finalizarTurno(Long motoristaId) {
        Motorista motorista = motoristaRepository.findById(motoristaId)
                .orElseThrow(() -> new RegraNegocioException("Motorista não encontrado."));
        motorista.setStatus(StatusMotorista.EM_DESCANSO);
        motorista.setFimUltimoTurno(LocalDateTime.now());
        return MotoristaDetalheDto.fromEntity(motoristaRepository.save(motorista));
    }

    public Motorista buscarEntidade(Long id) {
        return motoristaRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Motorista não encontrado."));
    }
}
