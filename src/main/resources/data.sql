-- Paradas
INSERT INTO parada (id, nome, cidade, estado) VALUES (10, 'Rio de Janeiro', 'Rio de Janeiro', 'RJ');
INSERT INTO parada (id, nome, cidade, estado) VALUES (11, 'Nova Iguaçu', 'Nova Iguaçu', 'RJ');
INSERT INTO parada (id, nome, cidade, estado) VALUES (12, 'Resende', 'Resende', 'RJ');
INSERT INTO parada (id, nome, cidade, estado) VALUES (13, 'São Paulo', 'São Paulo', 'SP');
INSERT INTO parada (id, nome, cidade, estado) VALUES (14, 'Campinas', 'Campinas', 'SP');

-- Rotas
INSERT INTO rotas (id, nome, preco_base) VALUES (1, 'Rio x Resende', 50.00);
INSERT INTO rotas (id, nome, preco_base) VALUES (2, 'São Paulo x Campinas', 35.00);

-- Itinerários
INSERT INTO rota_parada (id, rota_id, parada_id, ordem_parada, distancia_proxima_parada_km, parada_troca_motorista) VALUES (1, 1, 10, 0, 40.0, false);
INSERT INTO rota_parada (id, rota_id, parada_id, ordem_parada, distancia_proxima_parada_km, parada_troca_motorista) VALUES (2, 1, 11, 1, 120.0, true);
INSERT INTO rota_parada (id, rota_id, parada_id, ordem_parada, distancia_proxima_parada_km, parada_troca_motorista) VALUES (3, 1, 12, 2, 0.0, false);
INSERT INTO rota_parada (id, rota_id, parada_id, ordem_parada, distancia_proxima_parada_km, parada_troca_motorista) VALUES (4, 2, 13, 0, 95.0, false);
INSERT INTO rota_parada (id, rota_id, parada_id, ordem_parada, distancia_proxima_parada_km, parada_troca_motorista) VALUES (5, 2, 14, 1, 0.0, false);

-- Frota
INSERT INTO onibus (id, placa, capacidade, tipo, quilometragem_desde_ultima_revisao, quilometragem_total, status)
VALUES (1, 'CAL-2026', 23, 'LEITO', 0.0, 0.0, 'DISPONIVEL');
INSERT INTO onibus (id, placa, capacidade, tipo, quilometragem_desde_ultima_revisao, quilometragem_total, status)
VALUES (2, 'CAL-2027', 28, 'SEMI_LEITO', 0.0, 0.0, 'DISPONIVEL');
INSERT INTO onibus (id, placa, capacidade, tipo, quilometragem_desde_ultima_revisao, quilometragem_total, status)
VALUES (3, 'CAL-2028', 32, 'EXECUTIVO', 0.0, 0.0, 'DISPONIVEL');

-- Motoristas
INSERT INTO motorista (id, nome, cnh, status, horas_dirigidas_hoje, km_dirigidos_hoje)
VALUES (1, 'Carlos Souza', '12345678901', 'DISPONIVEL', 0.0, 0.0);
INSERT INTO motorista (id, nome, cnh, status, horas_dirigidas_hoje, km_dirigidos_hoje)
VALUES (2, 'Ana Pereira', '98765432109', 'DISPONIVEL', 0.0, 0.0);
INSERT INTO motorista (id, nome, cnh, status, horas_dirigidas_hoje, km_dirigidos_hoje)
VALUES (3, 'João Lima', '11223344556', 'DISPONIVEL', 0.0, 0.0);

-- Guichês
INSERT INTO guiche (id, nome, cidade, estado, parada_id, ativo)
VALUES (1, 'Guichê Rodoviária Rio', 'Rio de Janeiro', 'RJ', 10, true);
INSERT INTO guiche (id, nome, cidade, estado, parada_id, ativo)
VALUES (2, 'Guichê Rodoviária SP', 'São Paulo', 'SP', 13, true);

-- Viagens de exemplo
INSERT INTO viagens (id, rota_id, onibus_id, data_hora_saida, data_hora_chegada, status)
VALUES (1, 1, 1, '2026-07-15 08:00:00', '2026-07-15 12:00:00', 'PROGRAMADA');
INSERT INTO viagens (id, rota_id, onibus_id, data_hora_saida, data_hora_chegada, status)
VALUES (2, 1, 2, '2026-07-15 14:00:00', '2026-07-15 18:00:00', 'PROGRAMADA');
INSERT INTO viagens (id, rota_id, onibus_id, data_hora_saida, data_hora_chegada, status)
VALUES (3, 2, 3, '2026-07-15 09:00:00', '2026-07-15 11:00:00', 'PROGRAMADA');
