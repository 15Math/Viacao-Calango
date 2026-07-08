package com.viacao.calango.api.infrastructure.gateway;

import com.viacao.calango.api.domain.port.GatewayPagamento;
import com.viacao.calango.api.domain.enums.TipoPagamento;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class SimuladorPagamento implements GatewayPagamento {

    @Override
    public boolean processar(BigDecimal valor, TipoPagamento tipo) {
        System.out.println("Processando pagamento de R$ " + valor + " via " + tipo);

        //retorna sempre verdadeiro para testes
        return true;
    }
}