package com.viacao.calango.api.domain.service;

import com.viacao.calango.api.domain.entity.RotaParada;
import com.viacao.calango.api.domain.exception.RegraNegocioException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RotaUtilService {
    //verifica se tanto a origem quanto o destino realmente existem no itinerario
    public int[] resolverOrdensTrecho(List<RotaParada> itinerario, Long origemId, Long destinoId) {
        int ordemInicio = -1;
        int ordemFim = -1;
        for (int i = 0; i < itinerario.size(); i++) {
            if (itinerario.get(i).getParada().getId().equals(origemId)) {
                ordemInicio = i;
            }
            if (itinerario.get(i).getParada().getId().equals(destinoId)) {
                ordemFim = i;
            }
        }
        if (ordemInicio == -1 || ordemFim == -1 || ordemInicio >= ordemFim) {
            throw new RegraNegocioException("O trecho selecionado não faz parte do itinerário ou está na ordem incorreta.");
        }
        return new int[]{ordemInicio, ordemFim};
    }

    //verifica se o trajeto vai ser corrido completo
    public boolean isTrajetoCompleto(List<RotaParada> itinerario, Long origemId, Long destinoId) {
        return itinerario.get(0).getParada().getId().equals(origemId)
                && itinerario.get(itinerario.size() - 1).getParada().getId().equals(destinoId);
    }
}
