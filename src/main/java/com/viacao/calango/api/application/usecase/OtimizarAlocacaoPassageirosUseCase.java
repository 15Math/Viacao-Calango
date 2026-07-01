package com.viacao.calango.api.application.usecase;

import com.viacao.calango.api.domain.entity.Viagem;
import com.viacao.calango.api.domain.enums.StatusViagem;
import com.viacao.calango.api.domain.exception.RegraNegocioException;
import com.viacao.calango.api.infrastructure.repository.PassagemRepository;
import com.viacao.calango.api.infrastructure.repository.ViagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OtimizarAlocacaoPassageirosUseCase {

    private final ViagemRepository viagemRepository;
    private final PassagemRepository passagemRepository;
    private final GerenciarOnibusUseCase gerenciarOnibusUseCase;

    @Transactional(readOnly = true)
    public Map<String, Object> analisar(Long rotaId, LocalDate data) {
        List<Viagem> viagens = viagemRepository.findByRotaIdAndStatus(rotaId, StatusViagem.PROGRAMADA).stream()
                .filter(v -> v.getDataHoraSaida().toLocalDate().equals(data))
                .toList();

        if (viagens.isEmpty()) {
            throw new RegraNegocioException("Não há viagens programadas para esta rota na data informada.");
        }

        int totalPassageiros = viagens.stream()
                .mapToInt(v -> (int) passagemRepository.countByViagemId(v.getId()))
                .sum();

        var sugestao = gerenciarOnibusUseCase.sugerirOnibus(totalPassageiros);

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("totalPassageiros", totalPassageiros);
        resultado.put("viagensAnalisadas", viagens.size());
        resultado.put("sugestaoOnibus", sugestao);
        resultado.put("recomendacao", viagens.size() > 1 && totalPassageiros < viagens.size() * 10
                ? "Considere consolidar passageiros em menos viagens para reduzir lugares vazios."
                : "Distribuição atual dentro dos parâmetros esperados.");

        return resultado;
    }
}
