-- 1. Cadastrar as Paradas (Cidades)
INSERT INTO parada (id, nome, cidade, estado) VALUES (10, 'Rio de Janeiro', 'Rio de Janeiro', 'RJ');
INSERT INTO parada (id, nome, cidade, estado) VALUES (11, 'Nova Iguaçu', 'Nova Iguaçu', 'RJ');
INSERT INTO parada (id, nome, cidade, estado) VALUES (12, 'Resende', 'Resende', 'RJ');

-- 2. Cadastrar a Rota e o Preço Base
INSERT INTO rotas (id, nome, preco_base) VALUES (1, 'Rio x Resende', 50.00);

-- 3. Vincular as Paradas à Rota (Itinerário Ordenado)
INSERT INTO rota_parada (id, rota_id, parada_id, ordem_parada, distancia_proxima_parada_km) VALUES (1, 1, 10, 0, 40.0);
INSERT INTO rota_parada (id, rota_id, parada_id, ordem_parada, distancia_proxima_parada_km) VALUES (2, 1, 11, 1, 120.0);
INSERT INTO rota_parada (id, rota_id, parada_id, ordem_parada, distancia_proxima_parada_km) VALUES (3, 1, 12, 2, 0.0);

-- 4. Cadastrar um Ônibus (Tipo mapeado no Enum: LEITO, SEMI_LEITO, EXECUTIVO)
INSERT INTO onibus (id, placa, capacidade, tipo, quilometragem_desde_ultima_revisao, quilometragem_total)
VALUES (1, 'CAL-2026', 23, 'LEITO', 0.0, 0.0);

-- 5. Cadastrar um Motorista de teste (Status mapeado no Enum: DISPONIVEL)
INSERT INTO motorista (id, nome, cnh, status, horas_dirigidas_hoje, km_dirigidos_hoje)
VALUES (1, 'Carlos Souza', '123456789', 'DISPONIVEL', 0.0, 0.0);

-- 6. Cadastrar a Viagem vinculando a Rota e o Ônibus (Status: PROGRAMADA)
INSERT INTO viagens (id, rota_id, onibus_id, data_hora_saida, status)
VALUES (1, 1, 1, '2026-07-01 08:00:00', 'PROGRAMADA');

-- 7. Inicializar a matriz de ocupação de assentos livres para o Assento 1 nos subtrechos
INSERT INTO ocupacao_assento (id, viagem_id, origem_segmento_id, destino_segmento_id, numero_assento, ordem_segmento, status, versao)
VALUES (1, 1, 10, 11, 1, 0, 'LIVRE', 0);

INSERT INTO ocupacao_assento (id, viagem_id, origem_segmento_id, destino_segmento_id, numero_assento, ordem_segmento, status, versao)
VALUES (2, 1, 11, 12, 1, 1, 'LIVRE', 0);