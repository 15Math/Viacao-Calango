package com.viacao.calango.api.application.usecase;

import com.viacao.calango.api.domain.entity.Onibus;
import com.viacao.calango.api.domain.enums.StatusOnibus;
import com.viacao.calango.api.domain.exception.RegraNegocioException;
import com.viacao.calango.api.infrastructure.repository.OnibusRepository;
import com.viacao.calango.api.infrastructure.repository.ConfiguracaoSistemaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ControlarRevisaoUseCase {

    private final OnibusRepository onibusRepository;
    private final ConfiguracaoSistemaRepository configuracaoRepository;

    private static final double LIMITE_REVISAO_KM = 10000.0;

    @Transactional
    public void registrarFimViagem(Long onibusId, Double kmRodados) {
        Onibus onibus = onibusRepository.findById(onibusId)
                .orElseThrow(() -> new RegraNegocioException("Ônibus não encontrado."));

        onibus.setQuilometragemTotal(onibus.getQuilometragemTotal() + kmRodados);
        onibus.setQuilometragemDesdeUltimaRevisao(onibus.getQuilometragemDesdeUltimaRevisao() + kmRodados);

        double limiteRevisaoKm = configuracaoRepository.findByChave("LIMITE_REVISAO_KM")
                .map(config -> Double.parseDouble(config.getValor()))
                .orElse(LIMITE_REVISAO_KM);

        if (onibus.getQuilometragemDesdeUltimaRevisao() >= limiteRevisaoKm) {
            onibus.setStatus(StatusOnibus.EM_REVISAO);
            log.error("BLOQUEIO DE FROTA: O ônibus placa {} atingiu {} km desde a última revisão.",
                    onibus.getPlaca(), onibus.getQuilometragemDesdeUltimaRevisao());
        } else {
            onibus.setStatus(StatusOnibus.DISPONIVEL);
        }

        onibusRepository.save(onibus);
    }

    @Transactional
    public void realizarRevisao(Long onibusId) {
        Onibus onibus = onibusRepository.findById(onibusId)
                .orElseThrow(() -> new RegraNegocioException("Ônibus não encontrado."));

        onibus.setQuilometragemDesdeUltimaRevisao(0.0);
        onibus.setStatus(StatusOnibus.DISPONIVEL);
        onibusRepository.save(onibus);
        log.info("MANUTENÇÃO CONCLUÍDA: O ônibus placa {} está liberado para tráfego.", onibus.getPlaca());
    }
}
