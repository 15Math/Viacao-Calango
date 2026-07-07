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

-- Frota (Note as aspas na coluna "status")
INSERT INTO onibus (placa, capacidade, tipo, quilometragem_desde_ultima_revisao, quilometragem_total, "status")
VALUES ('CAL-2026', 23, 'LEITO', 0.0, 0.0, 'DISPONIVEL');
INSERT INTO onibus (placa, capacidade, tipo, quilometragem_desde_ultima_revisao, quilometragem_total, "status")
VALUES ('CAL-2027', 28, 'SEMI_LEITO', 0.0, 0.0, 'DISPONIVEL');
INSERT INTO onibus (placa, capacidade, tipo, quilometragem_desde_ultima_revisao, quilometragem_total, "status")
VALUES ('CAL-2028', 32, 'EXECUTIVO', 0.0, 0.0, 'DISPONIVEL');

-- Motoristas (Note as aspas na coluna "status")
INSERT INTO motorista (nome, cnh, "status", horas_dirigidas_hoje, km_dirigidos_hoje)
VALUES ('Carlos Souza', '12345678901', 'DISPONIVEL', 0.0, 0.0);
INSERT INTO motorista (nome, cnh, "status", horas_dirigidas_hoje, km_dirigidos_hoje)
VALUES ('Ana Pereira', '98765432109', 'DISPONIVEL', 0.0, 0.0);
INSERT INTO motorista (nome, cnh, "status", horas_dirigidas_hoje, km_dirigidos_hoje)
VALUES ('João Lima', '11223344556', 'DISPONIVEL', 0.0, 0.0);

-- Viagens de exemplo (Note as aspas na coluna "status")
INSERT INTO viagens (rota_id, onibus_id, data_hora_saida, data_hora_chegada, "status")
VALUES (1, 1, '2026-07-15 08:00:00', '2026-07-15 12:00:00', 'PROGRAMADA');
INSERT INTO viagens (rota_id, onibus_id, data_hora_saida, data_hora_chegada, "status")
VALUES (1, 2, '2026-07-15 14:00:00', '2026-07-15 18:00:00', 'PROGRAMADA');
INSERT INTO viagens (rota_id, onibus_id, data_hora_saida, data_hora_chegada, "status")
VALUES (2, 3, '2026-07-15 09:00:00', '2026-07-15 11:00:00', 'PROGRAMADA');

-- Constantes do sistema
INSERT INTO configuracao_sistema (chave, valor, descricao)
VALUES ('LIMITE_REVISAO_KM', '10000.0', 'Define a quilometragem máxima antes de bloquear o ônibus para revisão.');
INSERT INTO configuracao_sistema (chave, valor, descricao)
VALUES ('CAPACIDADES_PERMITIDAS', '23,28,32', 'Define as capacidades permitidas de assentos para novos ônibus cadastrados na frota.');
INSERT INTO configuracao_sistema (chave, valor, descricao)
VALUES ('FATOR_LEITO', '1.5', 'Multiplicador de preço para ônibus da categoria Leito.');
INSERT INTO configuracao_sistema (chave, valor, descricao)
VALUES ('FATOR_SEMI_LEITO', '1.2', 'Multiplicador de preço para ônibus da categoria Semi-Leito.');
INSERT INTO configuracao_sistema (chave, valor, descricao)
VALUES ('FATOR_EXECUTIVO', '1.0', 'Multiplicador de preço para ônibus da categoria Executivo.');
INSERT INTO configuracao_sistema (chave, valor, descricao)
VALUES ('DESCONTO_TRAJETO_COMPLETO', '0.15', 'Percentual de desconto caso o passageiro faça a rota de ponta a ponta (ex: 0.15 = 15%).');
INSERT INTO configuracao_sistema (chave, valor, descricao)
VALUES ('VELOCIDADE_MEDIA_KMH', '70.0', 'Velocidade media Km/h');
INSERT INTO configuracao_sistema (chave, valor, descricao)
VALUES ('LIMITE_KM_TURNO', '400.0', 'Limite de Km por turno');
INSERT INTO configuracao_sistema (chave, valor, descricao)
VALUES ('LIMITE_HORAS_TURNO', '6.0', 'Limite de horas por turno');

-- Registros estrategia de antecedencia
INSERT INTO faixa_antecedencia (dias_minimos, desconto) VALUES (30, 0.20);
INSERT INTO faixa_antecedencia (dias_minimos, desconto) VALUES (15, 0.10);
INSERT INTO faixa_antecedencia (dias_minimos, desconto) VALUES (7, 0.05);