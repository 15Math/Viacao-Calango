package com.viacao.calango.api.application.usecase;

import com.viacao.calango.api.application.dto.OnibusRequestDto;
import com.viacao.calango.api.application.dto.OnibusResponseDto;
import com.viacao.calango.api.application.dto.SugestaoOnibusDto;
import com.viacao.calango.api.domain.entity.Onibus;
import com.viacao.calango.api.domain.enums.StatusOnibus;
import com.viacao.calango.api.domain.exception.RegraNegocioException;
import com.viacao.calango.api.infrastructure.repository.OnibusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
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

    @Transactional(readOnly = true)
    public SugestaoOnibusDto sugerirOnibus(int passageirosEsperados) {
        List<Onibus> candidatos = onibusRepository.findByStatus(StatusOnibus.DISPONIVEL).stream()
                .filter(o -> !o.precisaRevisao())
                .filter(o -> o.getCapacidade() >= passageirosEsperados)
                .toList();

        if (candidatos.isEmpty()) {
            throw new RegraNegocioException("Não há ônibus disponíveis com capacidade para " + passageirosEsperados + " passageiros.");
        }

        Onibus melhor = candidatos.stream()
                .min(Comparator.comparingInt(o -> o.getCapacidade() - passageirosEsperados))
                .orElseThrow();

        int vazios = melhor.getCapacidade() - passageirosEsperados;
        String recomendacao = vazios == 0
                ? "Capacidade exata — ocupação ideal."
                : "Menor capacidade disponível com " + vazios + " lugares vazios estimados.";

        return new SugestaoOnibusDto(
                melhor.getId(),
                melhor.getPlaca(),
                melhor.getTipo(),
                melhor.getCapacidade(),
                vazios,
                recomendacao
        );
    }

    public Onibus buscarEntidade(Long id) {
        return onibusRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Ônibus não encontrado."));
    }
}
