package com.viacao.calango.api.infrastructure.config;

import com.viacao.calango.api.application.usecase.InicializarViagemUseCase;
import com.viacao.calango.api.infrastructure.repository.ViagemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeedDataInitializer implements ApplicationRunner {

    private final ViagemRepository viagemRepository;
    private final InicializarViagemUseCase inicializarViagemUseCase;

    @Override
    public void run(ApplicationArguments args) {
        viagemRepository.findAll().forEach(viagem -> {
            inicializarViagemUseCase.garantirMatrizAssentos(viagem.getId());
            log.debug("Matriz de assentos verificada para viagem {}", viagem.getId());
        });
    }
}
