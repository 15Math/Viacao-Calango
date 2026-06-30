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
            log.warn("ALERTA DE MANUTENÇÃO: O ônibus placa {} atingiu {} km desde a última revisão. Encaminhar para garagem!",
                    onibus.getPlaca(), onibus.getQuilometragemDesdeUltimaRevisao());
            // Aqui você poderia alterar o status do ônibus para "EM_MANUTENCAO"
        }

        onibusRepository.save(onibus);
    }
}