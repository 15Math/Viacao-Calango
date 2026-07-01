package com.viacao.calango.api.application.usecase;

import com.viacao.calango.api.application.dto.*;
import com.viacao.calango.api.domain.entity.Onibus;
import com.viacao.calango.api.domain.entity.Rota;
import com.viacao.calango.api.domain.entity.Viagem;
import com.viacao.calango.api.domain.enums.StatusOnibus;
import com.viacao.calango.api.domain.enums.StatusViagem;
import com.viacao.calango.api.domain.exception.RegraNegocioException;
import com.viacao.calango.api.infrastructure.repository.PassagemRepository;
import com.viacao.calango.api.infrastructure.repository.ViagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GerenciarViagemUseCase {

    private final ViagemRepository viagemRepository;
    private final PassagemRepository passagemRepository;
    private final GerenciarRotaUseCase gerenciarRotaUseCase;
    private final GerenciarOnibusUseCase gerenciarOnibusUseCase;
    private final InicializarViagemUseCase inicializarViagemUseCase;

    @Transactional(readOnly = true)
    public List<ViagemResumoDto> buscar(Long origemId, Long destinoId, LocalDate data, Long rotaId) {
        List<Viagem> viagens;
        if (origemId != null && destinoId != null && data != null) {
            LocalDateTime inicio = data.atStartOfDay();
            LocalDateTime fim = data.plusDays(1).atStartOfDay();
            viagens = viagemRepository.buscarPorTrechoEData(origemId, destinoId, inicio, fim, StatusViagem.PROGRAMADA);
        } else if (rotaId != null) {
            viagens = viagemRepository.findByRotaIdAndStatus(rotaId, StatusViagem.PROGRAMADA);
        } else {
            viagens = viagemRepository.findAll();
        }

        return viagens.stream()
                .map(v -> {
                    long vendidos = passagemRepository.countByViagemId(v.getId());
                    int disponiveis = v.getOnibus().getCapacidade() - (int) vendidos;
                    return ViagemResumoDto.fromEntity(v, vendidos, Math.max(disponiveis, 0));
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public ViagemDetalheDto buscarDetalhe(Long id) {
        Viagem viagem = viagemRepository.findDetalhadaById(id)
                .orElseThrow(() -> new RegraNegocioException("Viagem não encontrada."));
        long total = passagemRepository.countByViagemId(id);
        return ViagemDetalheDto.fromEntity(viagem, total);
    }

    @Transactional
    public ViagemDetalheDto criar(CriarViagemRequestDto request) {
        Rota rota = gerenciarRotaUseCase.buscarEntidade(request.rotaId());
        Onibus onibus = gerenciarOnibusUseCase.buscarEntidade(request.onibusId());

        if (onibus.precisaRevisao() || onibus.getStatus() == StatusOnibus.EM_REVISAO) {
            throw new RegraNegocioException("O ônibus " + onibus.getPlaca() + " está bloqueado para revisão.");
        }
        if (onibus.getStatus() != StatusOnibus.DISPONIVEL) {
            throw new RegraNegocioException("O ônibus " + onibus.getPlaca() + " não está disponível.");
        }

        Viagem viagem = new Viagem();
        viagem.setRota(rota);
        viagem.setOnibus(onibus);
        viagem.setDataHoraSaida(request.dataHoraSaida());
        viagem.setDataHoraChegada(request.dataHoraChegada());
        viagem.setStatus(StatusViagem.PROGRAMADA);

        Viagem criada = inicializarViagemUseCase.ejecutar(viagem);
        onibus.setStatus(StatusOnibus.EM_VIAGEM);
        return ViagemDetalheDto.fromEntity(criada, 0);
    }

    @Transactional
    public ViagemDetalheDto atualizarStatus(Long id, StatusViagem novoStatus) {
        Viagem viagem = viagemRepository.findDetalhadaById(id)
                .orElseThrow(() -> new RegraNegocioException("Viagem não encontrada."));
        viagem.setStatus(novoStatus);

        if (novoStatus == StatusViagem.CONCLUIDA || novoStatus == StatusViagem.CANCELADA) {
            viagem.getOnibus().setStatus(StatusOnibus.DISPONIVEL);
        }

        Viagem salva = viagemRepository.save(viagem);
        long total = passagemRepository.countByViagemId(id);
        return ViagemDetalheDto.fromEntity(salva, total);
    }

    public Viagem buscarEntidade(Long id) {
        return viagemRepository.findDetalhadaById(id)
                .orElseThrow(() -> new RegraNegocioException("Viagem não encontrada."));
    }
}
