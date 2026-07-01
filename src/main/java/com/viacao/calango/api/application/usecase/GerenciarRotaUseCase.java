package com.viacao.calango.api.application.usecase;

import com.viacao.calango.api.application.dto.RotaRequestDto;
import com.viacao.calango.api.application.dto.RotaResponseDto;
import com.viacao.calango.api.domain.entity.Parada;
import com.viacao.calango.api.domain.entity.Rota;
import com.viacao.calango.api.domain.entity.RotaParada;
import com.viacao.calango.api.domain.exception.RegraNegocioException;
import com.viacao.calango.api.infrastructure.repository.ParadaRepository;
import com.viacao.calango.api.infrastructure.repository.RotaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GerenciarRotaUseCase {

    private final RotaRepository rotaRepository;
    private final ParadaRepository paradaRepository;

    @Transactional(readOnly = true)
    public List<RotaResponseDto> listar() {
        return rotaRepository.findAllComItinerario().stream().map(RotaResponseDto::fromEntity).toList();
    }

    @Transactional
    public RotaResponseDto criar(RotaRequestDto request) {
        Rota rota = new Rota();
        rota.setNome(request.nome());
        rota.setPrecoBase(request.precoBase());

        for (var item : request.itinerario()) {
            Parada parada = paradaRepository.findById(item.paradaId())
                    .orElseThrow(() -> new RegraNegocioException("Parada " + item.paradaId() + " não encontrada."));
            RotaParada rp = new RotaParada();
            rp.setRota(rota);
            rp.setParada(parada);
            rp.setOrdemParada(item.ordemParada());
            rp.setDistanciaProximaParadaKm(item.distanciaProximaParadaKm());
            rp.setParadaTrocaMotorista(Boolean.TRUE.equals(item.paradaTrocaMotorista()));
            rota.getItinerario().add(rp);
        }

        return RotaResponseDto.fromEntity(rotaRepository.save(rota));
    }

    public Rota buscarEntidade(Long id) {
        return rotaRepository.findComItinerarioById(id)
                .orElseThrow(() -> new RegraNegocioException("Rota não encontrada."));
    }
}
