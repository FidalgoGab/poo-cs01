-- ==============================================================================
-- FIAP MOTIVA - SPRINT 4
-- SCRIPT DE CONSULTAS (seu-script-consultas.sql)
--
-- Descrição:
--   Consultas SQL avançadas explorando o modelo relacional do projeto,
--   com foco especial nas junções N:M (equipe_membro e trecho_intervencao),
--   regras de negócio do motor de priorização de roçada e relatórios gerenciais.
-- ==============================================================================

-- ------------------------------------------------------------------------------
-- CONSULTA 1: MEMBROS POR EQUIPE (Junção N:M)
-- Tabela associativa: equipe_membro
-- Tabelas envolvidas: equipes_manutencao, equipe_membro, pessoas
-- Objetivo: Listar cada equipe e todos os seus colaboradores alocados,
--           incluindo cálculo dinâmico da idade a partir da data de nascimento.
-- ------------------------------------------------------------------------------
SELECT
    e.id_equipe,
    e.nome AS equipe,
    e.descricao AS descricao_equipe,
    p.documento AS documento_membro,
    p.nome AS nome_membro,
    TO_CHAR(p.data_nascimento, 'DD/MM/YYYY') AS data_nascimento,
    TRUNC(MONTHS_BETWEEN(SYSDATE, p.data_nascimento) / 12) AS idade_anos
FROM
    equipes_manutencao e
    JOIN equipe_membro em ON em.id_equipe = e.id_equipe
    JOIN pessoas p ON p.documento = em.documento
ORDER BY
    e.id_equipe ASC,
    p.nome ASC;

-- ------------------------------------------------------------------------------
-- CONSULTA 2: INTERVENÇÕES PROGRAMADAS POR TRECHO (Junção N:M)
-- Tabela associativa: trecho_intervencao
-- Tabelas envolvidas: trechos_rodovia, trecho_intervencao, intervencao_operacional, equipes_manutencao
-- Objetivo: Demonstrar os trechos de rodovia que possuem intervenções operacionais
--           agendadas (ex: Roçada Mecanizada, Pulverização), nível de vegetação
--           atual, tipo de trecho, data programada e a equipe responsável.
-- ------------------------------------------------------------------------------
SELECT
    t.id_trecho,
    t.nome_trecho,
    t.tipo_trecho,
    t.nivel_vegetacao_cm,
    (t.km_final - t.km_inicial) AS extensao_km,
    CASE 
        WHEN t.monitoravel_iot = 1 THEN 'SIM' 
        ELSE 'NÃO' 
    END AS possui_sensor_iot,
    i.nome AS tipo_intervencao,
    i.descricao AS descricao_intervencao,
    TO_CHAR(ti.data_programada, 'DD/MM/YYYY') AS data_programada,
    ti.observacao AS observacao_operacional,
    NVL(e.nome, 'SEM EQUIPE ATRIBUÍDA') AS equipe_responsavel
FROM
    trechos_rodovia t
    JOIN trecho_intervencao ti ON ti.id_trecho = t.id_trecho
    JOIN intervencao_operacional i ON i.id_intervencao = ti.id_intervencao
    LEFT JOIN equipes_manutencao e ON e.id_equipe = t.id_equipe
ORDER BY
    t.nivel_vegetacao_cm DESC,
    ti.data_programada ASC;

-- ------------------------------------------------------------------------------
-- CONSULTA 3: VISÃO INTEGRADA - TRECHO, EQUIPE RESPONSÁVEL E COLABORADORES
-- Tabelas envolvidas: trechos_rodovia, equipes_manutencao, equipe_membro, pessoas
-- Objetivo: Obter uma visão de ponta a ponta: para cada trecho, qual equipe
--           está alocada e quais são as pessoas físicas que compõem a equipe.
-- ------------------------------------------------------------------------------
SELECT
    t.id_trecho,
    t.nome_trecho,
    t.tipo_trecho,
    t.nivel_vegetacao_cm,
    NVL(e.nome, 'NÃO ATRIBUÍDO') AS equipe_manutencao,
    NVL(p.nome, 'SEM MEMBRO CADASTRADO') AS membro_equipe,
    NVL(p.documento, '-') AS documento
FROM
    trechos_rodovia t
    LEFT JOIN equipes_manutencao e ON e.id_equipe = t.id_equipe
    LEFT JOIN equipe_membro em ON em.id_equipe = e.id_equipe
    LEFT JOIN pessoas p ON p.documento = em.documento
ORDER BY
    t.id_trecho ASC,
    p.nome ASC;

-- ------------------------------------------------------------------------------
-- CONSULTA 4: ANÁLISE DE CRITICIDADE E CLASSIFICAÇÃO PELO MOTOR DE REGRAS
-- Tabelas envolvidas: trechos_rodovia, trecho_intervencao, intervencao_operacional
-- Objetivo: Mapear a regra de negócio da aplicação diretamente via SQL,
--           classificando a prioridade de roçada e verificando se já há
--           intervenção cadastrada na tabela associativa N:M.
-- ------------------------------------------------------------------------------
SELECT
    t.id_trecho,
    t.nome_trecho,
    t.tipo_trecho,
    t.nivel_vegetacao_cm,
    CASE
        WHEN t.nivel_vegetacao_cm >= 50.0 THEN 'ALTA (URGENTE)'
        WHEN t.nivel_vegetacao_cm >= 30.0 THEN 'MÉDIA (CRÍTICO)'
        WHEN t.nivel_vegetacao_cm >= 15.0 THEN 'ATENÇÃO (MONITORAR)'
        ELSE 'BAIXA (NORMAL)'
    END AS classificacao_prioridade,
    CASE
        WHEN t.nivel_vegetacao_cm >= 50.0 THEN 'ROCADA_MECANIZADA'
        WHEN t.nivel_vegetacao_cm >= 30.0 AND t.tipo_trecho = 'UMIDO' THEN 'PULVERIZACAO'
        WHEN t.nivel_vegetacao_cm >= 30.0 THEN 'ROCADA_MANUAL'
        ELSE 'NENHUMA AÇÃO IMEDIATA'
    END AS intervencao_sugerida,
    NVL(i.nome, 'PENDENTE DE AGENDAMENTO') AS intervencao_registrada
FROM
    trechos_rodovia t
    LEFT JOIN trecho_intervencao ti ON ti.id_trecho = t.id_trecho
    LEFT JOIN intervencao_operacional i ON i.id_intervencao = ti.id_intervencao
ORDER BY
    t.nivel_vegetacao_cm DESC;

-- ------------------------------------------------------------------------------
-- CONSULTA 5: RESUMO ESTATÍSTICO POR EQUIPE (Agregações)
-- Tabelas envolvidas: equipes_manutencao, equipe_membro, trechos_rodovia
-- Objetivo: Apresentar a carga de trabalho de cada equipe: quantidade de membros,
--           quantidade de trechos sob gestão e média de vegetação acumulada.
-- ------------------------------------------------------------------------------
SELECT
    e.id_equipe,
    e.nome AS equipe,
    COUNT(DISTINCT em.documento) AS total_membros,
    COUNT(DISTINCT t.id_trecho) AS total_trechos_atribuidos,
    NVL(ROUND(AVG(t.nivel_vegetacao_cm), 2), 0.00) AS media_vegetacao_cm
FROM
    equipes_manutencao e
    LEFT JOIN equipe_membro em ON em.id_equipe = e.id_equipe
    LEFT JOIN trechos_rodovia t ON t.id_equipe = e.id_equipe
GROUP BY
    e.id_equipe,
    e.nome
ORDER BY
    total_trechos_atribuidos DESC,
    e.id_equipe ASC;

-- ------------------------------------------------------------------------------
-- CONSULTA 6: TRECHOS CRÍTICOS COM SENSORES IOT PENDENTES DE INTERVENÇÃO
-- Tabelas envolvidas: trechos_rodovia, trecho_intervencao
-- Objetivo: Identificar pontos de risco na rodovia onde os sensores IoT acusam
--           vegetação alta (>= 30 cm), porém nenhuma intervenção foi cadastrada.
-- ------------------------------------------------------------------------------
SELECT
    t.id_trecho,
    t.nome_trecho,
    t.km_inicial,
    t.km_final,
    t.nivel_vegetacao_cm,
    t.tipo_trecho
FROM
    trechos_rodovia t
    LEFT JOIN trecho_intervencao ti ON ti.id_trecho = t.id_trecho
WHERE
    t.monitoravel_iot = 1
    AND t.nivel_vegetacao_cm >= 30.0
    AND ti.id_intervencao IS NULL
ORDER BY
    t.nivel_vegetacao_cm DESC;

-- ------------------------------------------------------------------------------
-- CONSULTA 7: HISTÓRICO DE RELATÓRIOS DE PRIORIDADE GERADOS
-- Tabela envolvida: relatorio_prioridade
-- Objetivo: Auditoria dos relatórios consolidados pelo serviço GeradorRelatorio,
--           com totalização de trechos analisados por execução.
-- ------------------------------------------------------------------------------
SELECT
    r.id_relatorio,
    TO_CHAR(r.data_geracao, 'DD/MM/YYYY HH24:MI:SS') AS data_hora_geracao,
    r.qt_urgente,
    r.qt_critico,
    r.qt_atencao,
    r.qt_normal,
    (r.qt_urgente + r.qt_critico + r.qt_atencao + r.qt_normal) AS total_trechos_analisados,
    r.resumo
FROM
    relatorio_prioridade r
ORDER BY
    r.id_relatorio DESC;
