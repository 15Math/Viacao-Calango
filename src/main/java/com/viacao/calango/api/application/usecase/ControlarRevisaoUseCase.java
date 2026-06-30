package com.viacao.calango.api.application.usecase;

import com.viacao.calango.api.domain.entity.Onibus;
import com.viacao.calango.api.domain.exception.RegraNegocioException;
import com.viacao.calango.api.infrastructure.repository.OnibusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ControlarRevisaoUseCase {

    private final OnibusRepository onibusRepository;
    private static final double LIMITE_REVISAO_KM = 10000.0;

    @Transactional
    public void registrarFimViagem(Long onibusId, Double kmRodados) {
        Onibus onibus = onibusRepository.findById(onibusId)
                .orElseThrow(() -> new RegraNegocioException("Ônibus não encontrado."));

        onibus.setQuilometragemTotal(onibus.getQuilometragemTotal() + kmRodados);
        onibus.setQuilometragemDesdeUltimaRevisao(onibus.getQuilometragemDesdeUltimaRevisao() + kmRodados);

        if (onibus.getQuilometragemDesdeUltimaRevisao() >= LIMITE_REVISAO_KM) {
            log.error("BLOQUEIO DE FROTA: O ônibus placa {} atingiu {} km desde a última revisão. Alocações impedidas até a manutenção!",
                    onibus.getPlaca(), onibus.getQuilometragemDesdeUltimaRevisao());
        }

        onibusRepository.save(onibus);
    }

    //Permitir dar baixa na oficina e liberar o veiculo para novas viagens
    @Transactional
    public void realizarRevisao(Long onibusId) {
        Onibus onibus = onibusRepository.findById(onibusId)
                .orElseThrow(() -> new RegraNegocioException("Ônibus não encontrado."));

        onibus.setQuilometragemDesdeUltimaRevisao(0.0);
        onibusRepository.save(onibus);
        log.info("MANUTENÇÃO CONCLUÍDA: O ônibus placa {} teve seu contador zerado e está liberado para tráfego.", onibus.getPlaca());
    }
}