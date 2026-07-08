package com.viacao.calango.api.domain.port;

import com.viacao.calango.api.domain.enums.TipoPagamento;

import java.math.BigDecimal;

public interface GatewayPagamento {
    boolean processar(BigDecimal valor, TipoPagamento tipo);
}
