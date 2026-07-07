-- Paradas
INSERT INTO parada (nome, cidade, estado) VALUES ('Rio de Janeiro', 'Rio de Janeiro', 'RJ');
INSERT INTO parada (nome, cidade, estado) VALUES ('Nova Iguaçu', 'Nova Iguaçu', 'RJ');
INSERT INTO parada (nome, cidade, estado) VALUES ('Resende', 'Resende', 'RJ');
INSERT INTO parada (nome, cidade, estado) VALUES ('São Paulo', 'São Paulo', 'SP');
INSERT INTO parada (nome, cidade, estado) VALUES ('Campinas', 'Campinas', 'SP');

-- Rotas
INSERT INTO rotas (nome, preco_base) VALUES ('Rio x Resende', 50.00);
INSERT INTO rotas (nome, preco_base) VALUES ('São Paulo x Campinas', 35.00);

-- Itinerários
INSERT INTO rota_parada (rota_id, parada_id, ordem_parada, distancia_proxima_parada_km, parada_troca_motorista) VALUES (1, 1, 0, 40.0, false);
INSERT INTO rota_parada (rota_id, parada_id, ordem_parada, distancia_proxima_parada_km, parada_troca_motorista) VALUES (1, 2, 1, 120.0, true);
INSERT INTO rota_parada (rota_id, parada_id, ordem_parada, distancia_proxima_parada_km, parada_troca_motorista) VALUES (1, 3, 2, 0.0, false);
INSERT INTO rota_parada (rota_id, parada_id, ordem_parada, distancia_proxima_parada_km, parada_troca_motorista) VALUES (2, 4, 0, 95.0, false);
INSERT INTO rota_parada (rota_id, parada_id, ordem_parada, distancia_proxima_parada_km, parada_troca_motorista) VALUES (2, 5, 1, 0.0, false);

-- Frota
INSERT INTO onibus (placa, capacidade, tipo, quilometragem_desde_ultima_revisao, quilometragem_total, "status")
VALUES ('CAL-2026', 23, 'LEITO', 0.0, 0.0, 'DISPONIVEL');
INSERT INTO onibus (placa, capacidade, tipo, quilometragem_desde_ultima_revisao, quilometragem_total, "status")
VALUES ('CAL-2027', 28, 'SEMI_LEITO', 0.0, 0.0, 'DISPONIVEL');
INSERT INTO onibus (placa, capacidade, tipo, quilometragem_desde_ultima_revisao, quilometragem_total, "status")
VALUES ('CAL-2028', 32, 'EXECUTIVO', 0.0, 0.0, 'DISPONIVEL');

-- Motoristas
INSERT INTO motorista (nome, cnh, "status", horas_dirigidas_hoje, km_dirigidos_hoje)
VALUES ('Carlos Souza', '12345678901', 'DISPONIVEL', 0.0, 0.0);
INSERT INTO motorista (nome, cnh, "status", horas_dirigidas_hoje, km_dirigidos_hoje)
VALUES ('Ana Pereira', '98765432109', 'DISPONIVEL', 0.0, 0.0);
INSERT INTO motorista (nome, cnh, "status", horas_dirigidas_hoje, km_dirigidos_hoje)
VALUES ('João Lima', '11223344556', 'DISPONIVEL', 0.0, 0.0);

-- Viagens
INSERT INTO viagens (rota_id, onibus_id, data_hora_saida, data_hora_chegada, "status")
VALUES (1, 1, '2026-07-15 08:00:00', '2026-07-15 12:00:00', 'PROGRAMADA');
INSERT INTO viagens (rota_id, onibus_id, data_hora_saida, data_hora_chegada, "status")
VALUES (1, 2, '2026-07-15 14:00:00', '2026-07-15 18:00:00', 'PROGRAMADA');
INSERT INTO viagens (rota_id, onibus_id, data_hora_saida, data_hora_chegada, "status")
VALUES (2, 3, '2026-07-15 09:00:00', '2026-07-15 11:00:00', 'PROGRAMADA');

-- Constantes do sistema
INSERT INTO configuracao_sistema (chave, valor, descricao) VALUES ('LIMITE_REVISAO_KM', '10000.0', 'Limite de KM');
INSERT INTO configuracao_sistema (chave, valor, descricao) VALUES ('CAPACIDADES_PERMITIDAS', '23,28,32', 'Capacidades');
INSERT INTO configuracao_sistema (chave, valor, descricao) VALUES ('FATOR_LEITO', '1.5', 'Multiplicador');
INSERT INTO configuracao_sistema (chave, valor, descricao) VALUES ('FATOR_SEMI_LEITO', '1.2', 'Multiplicador');
INSERT INTO configuracao_sistema (chave, valor, descricao) VALUES ('FATOR_EXECUTIVO', '1.0', 'Multiplicador');
INSERT INTO configuracao_sistema (chave, valor, descricao) VALUES ('DESCONTO_TRAJETO_COMPLETO', '0.15', 'Desconto');
INSERT INTO configuracao_sistema (chave, valor, descricao) VALUES ('VELOCIDADE_MEDIA_KMH', '70.0', 'Velocidade');
INSERT INTO configuracao_sistema (chave, valor, descricao) VALUES ('LIMITE_KM_TURNO', '400.0', 'Limite KM');
INSERT INTO configuracao_sistema (chave, valor, descricao) VALUES ('LIMITE_HORAS_TURNO', '6.0', 'Limite horas');

-- Faixas
INSERT INTO faixa_antecedencia (dias_minimos, desconto) VALUES (30, 0.20);
INSERT INTO faixa_antecedencia (dias_minimos, desconto) VALUES (15, 0.10);
INSERT INTO faixa_antecedencia (dias_minimos, desconto) VALUES (7, 0.05);

-- Ocupação de Assentos (Adicionada a coluna "versao" com valor 0)
INSERT INTO ocupacao_assento (viagem_id, numero_assento, ordem_segmento, "status", origem_segmento_id, destino_segmento_id, versao)
VALUES (1, 1, 0, 'LIVRE', 1, 2, 0);
INSERT INTO ocupacao_assento (viagem_id, numero_assento, ordem_segmento, "status", origem_segmento_id, destino_segmento_id, versao)
VALUES (1, 1, 1, 'LIVRE', 2, 3, 0);
INSERT INTO ocupacao_assento (viagem_id, numero_assento, ordem_segmento, "status", origem_segmento_id, destino_segmento_id, versao)
VALUES (1, 2, 0, 'LIVRE', 1, 2, 0);
INSERT INTO ocupacao_assento (viagem_id, numero_assento, ordem_segmento, "status", origem_segmento_id, destino_segmento_id, versao)
VALUES (1, 2, 1, 'LIVRE', 2, 3, 0);
INSERT INTO ocupacao_assento (viagem_id, numero_assento, ordem_segmento, "status", origem_segmento_id, destino_segmento_id, versao)
VALUES (1, 3, 0, 'LIVRE', 1, 2, 0);
INSERT INTO ocupacao_assento (viagem_id, numero_assento, ordem_segmento, "status", origem_segmento_id, destino_segmento_id, versao)
VALUES (1, 3, 1, 'LIVRE', 2, 3, 0);
INSERT INTO ocupacao_assento (viagem_id, numero_assento, ordem_segmento, "status", origem_segmento_id, destino_segmento_id, versao)
VALUES (1, 4, 0, 'LIVRE', 1, 2, 0);
INSERT INTO ocupacao_assento (viagem_id, numero_assento, ordem_segmento, "status", origem_segmento_id, destino_segmento_id, versao)
VALUES (1, 4, 1, 'LIVRE', 2, 3, 0);
INSERT INTO ocupacao_assento (viagem_id, numero_assento, ordem_segmento, "status", origem_segmento_id, destino_segmento_id, versao)
VALUES (1, 5, 0, 'LIVRE', 1, 2, 0);
INSERT INTO ocupacao_assento (viagem_id, numero_assento, ordem_segmento, "status", origem_segmento_id, destino_segmento_id, versao)
VALUES (1, 5, 1, 'LIVRE', 2, 3, 0);