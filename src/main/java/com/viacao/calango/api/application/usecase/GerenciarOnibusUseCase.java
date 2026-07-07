package com.viacao.calango.api.application.usecase;

import com.viacao.calango.api.application.dto.OnibusRequestDto;
import com.viacao.calango.api.application.dto.OnibusResponseDto;
import com.viacao.calango.api.domain.enums.TipoOnibus;
import com.viacao.calango.api.domain.enums.StatusOnibus;
import com.viacao.calango.api.domain.entity.Onibus;
import com.viacao.calango.api.domain.exception.RegraNegocioException;
import com.viacao.calango.api.infrastructure.repository.ConfiguracaoSistemaRepository;
import com.viacao.calango.api.infrastructure.repository.OnibusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GerenciarOnibusUseCase {

    private final OnibusRepository onibusRepository;
    private final ConfiguracaoSistemaRepository configuracaoRepository;

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

    public Onibus buscarEntidade(Long id) {
        return onibusRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Ônibus não encontrado."));
    }

    @Transactional
    public OnibusResponseDto cadastrar(OnibusRequestDto request) {
        if (onibusRepository.existsByPlaca(request.placa())) {
            throw new RegraNegocioException("Já existe um ônibus cadastrado com a placa " + request.placa());
        }

        //verificar se esta na capacidade permitida
        String capacidadesPermitidasStr = configuracaoRepository.findByChave("CAPACIDADES_PERMITIDAS")
                .map(config -> config.getValor())
                .orElse("23,28,32");

        List<Integer> capacidadesPermitidas = Arrays.stream(capacidadesPermitidasStr.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .toList();

        if (!capacidadesPermitidas.contains(request.capacidade())) {
            throw new RegraNegocioException("A capacidade fornecida (" + request.capacidade() +
                    ") não é permitida. Valores aceitos: " + capacidadesPermitidasStr);
        }

        Onibus novoOnibus = new Onibus();
        novoOnibus.setPlaca(request.placa());
        novoOnibus.setCapacidade(request.capacidade());
        novoOnibus.setTipo(TipoOnibus.valueOf(request.tipo()));
        novoOnibus.setStatus(StatusOnibus.DISPONIVEL);
        novoOnibus.setQuilometragemTotal(0.0);
        novoOnibus.setQuilometragemDesdeUltimaRevisao(0.0);

        Onibus onibusSalvo = onibusRepository.save(novoOnibus);

        return OnibusResponseDto.fromEntity(onibusSalvo);
    }
}
