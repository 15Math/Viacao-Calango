package com.viacao.calango.api.application.usecase;

import com.viacao.calango.api.application.dto.OnibusRequestDto;
import com.viacao.calango.api.application.dto.OnibusResponseDto;
import com.viacao.calango.api.domain.entity.Onibus;
import com.viacao.calango.api.domain.enums.StatusOnibus;
import com.viacao.calango.api.domain.exception.RegraNegocioException;
import com.viacao.calango.api.infrastructure.repository.OnibusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GerenciarOnibusUseCase {

    private final OnibusRepository onibusRepository;

    @Transactional(readOnly = true)
    public List<OnibusResponseDto> listar() {
        return onibusRepository.findAll().stream().map(OnibusResponseDto::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public List<OnibusResponseDto> listarRevisaoPendente() {
        return onibusRepository.findAll().stream()
                .filter(Onibus::precisaRevisao)
                .map(OnibusResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OnibusResponseDto> listarDisponiveis() {
        return onibusRepository.findByStatus(StatusOnibus.DISPONIVEL).stream()
                .filter(o -> !o.precisaRevisao())
                .map(OnibusResponseDto::fromEntity)
                .toList();
    }

    @Transactional
    public OnibusResponseDto criar(OnibusRequestDto request) {
        Onibus onibus = new Onibus();
        onibus.setPlaca(request.placa());
        onibus.setCapacidade(request.capacidade());
        onibus.setTipo(request.tipo());
        onibus.setQuilometragemTotal(0.0);
        onibus.setQuilometragemDesdeUltimaRevisao(0.0);
        onibus.setStatus(StatusOnibus.DISPONIVEL);
        return OnibusResponseDto.fromEntity(onibusRepository.save(onibus));
    }

    public Onibus buscarEntidade(Long id) {
        return onibusRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Ônibus não encontrado."));
    }
}
