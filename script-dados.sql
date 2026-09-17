INSERT INTO
    equipes_manutencao (nome, descricao)
VALUES (
        'Equipe Norte',
        'Responsável por trechos críticos e monitorados da BR-116'
    );

INSERT INTO
    equipes_manutencao (nome, descricao)
VALUES (
        'Equipe Sul',
        'Atuação em trechos de baixa e média criticidade'
    );

INSERT INTO
    pessoas (
        documento,
        nome,
        data_nascimento
    )
VALUES (
        '12345678901',
        'Gabriel Fidalgo',
        TO_DATE ('1990-08-06', 'YYYY-MM-DD')
    );

INSERT INTO
    pessoas (
        documento,
        nome,
        data_nascimento
    )
VALUES (
        '23456789012',
        'Gustavo Rossi',
        TO_DATE ('1988-03-15', 'YYYY-MM-DD')
    );

INSERT INTO
    pessoas (
        documento,
        nome,
        data_nascimento
    )
VALUES (
        '34567890123',
        'Pedro Lima',
        TO_DATE ('1995-11-02', 'YYYY-MM-DD')
    );

INSERT INTO
    pessoas (
        documento,
        nome,
        data_nascimento
    )
VALUES (
        '45678901234',
        'Gustavo Maia',
        TO_DATE ('1992-06-21', 'YYYY-MM-DD')
    );

INSERT INTO
    equipe_membro (id_equipe, documento)
VALUES (1, '12345678901');

INSERT INTO
    equipe_membro (id_equipe, documento)
VALUES (1, '23456789012');

INSERT INTO
    equipe_membro (id_equipe, documento)
VALUES (2, '34567890123');

INSERT INTO
    equipe_membro (id_equipe, documento)
VALUES (2, '45678901234');

INSERT INTO
    trechos_rodovia (
        nome_trecho,
        km_inicial,
        km_final,
        nivel_vegetacao_cm,
        tipo_trecho,
        monitoravel_iot,
        id_equipe
    )
VALUES (
        'BR-116 - KM 10 a 20',
        10,
        20,
        22.50,
        'SECO',
        0,
        2
    );

INSERT INTO
    trechos_rodovia (
        nome_trecho,
        km_inicial,
        km_final,
        nivel_vegetacao_cm,
        tipo_trecho,
        monitoravel_iot,
        id_equipe
    )
VALUES (
        'BR-116 - KM 21 a 35',
        21,
        35,
        35.00,
        'UMIDO',
        1,
        1
    );

INSERT INTO
    trechos_rodovia (
        nome_trecho,
        km_inicial,
        km_final,
        nivel_vegetacao_cm,
        tipo_trecho,
        monitoravel_iot,
        id_equipe
    )
VALUES (
        'BR-116 - KM 36 a 48',
        36,
        48,
        58.75,
        'SECO',
        0,
        1
    );

INSERT INTO
    trechos_rodovia (
        nome_trecho,
        km_inicial,
        km_final,
        nivel_vegetacao_cm,
        tipo_trecho,
        monitoravel_iot,
        id_equipe
    )
VALUES (
        'BR-116 - KM 49 a 60',
        49,
        60,
        12.40,
        'UMIDO',
        1,
        2
    );

INSERT INTO
    intervencao_operacional (nome, descricao)
VALUES (
        'ROCADA_MECANIZADA',
        'Intervenção com máquinas para remoção de vegetação alta'
    );

INSERT INTO
    intervencao_operacional (nome, descricao)
VALUES (
        'PULVERIZACAO',
        'Aplicação de produto para controle de vegetação em trechos úmidos'
    );

INSERT INTO
    trecho_intervencao (
        id_trecho,
        id_intervencao,
        data_programada,
        observacao
    )
VALUES (
        2,
        2,
        SYSDATE,
        'Atenção ao crescimento acelerado em trecho úmido'
    );

INSERT INTO
    trecho_intervencao (
        id_trecho,
        id_intervencao,
        data_programada,
        observacao
    )
VALUES (
        3,
        1,
        SYSDATE,
        'Trecho prioritário para roçada mecanizada'
    );

INSERT INTO
    relatorio_prioridade (
        qt_urgente,
        qt_critico,
        qt_atencao,
        qt_normal,
        resumo,
        data_geracao
    )
VALUES (
        1,
        1,
        1,
        1,
        'Resumo de teste: 1 urgente, 1 crítico, 1 atenção, 1 normal.',
        SYSDATE
    );

COMMIT;

SELECT e.nome AS equipe, p.nome AS membro, t.nome_trecho, t.tipo_trecho, t.nivel_vegetacao_cm
FROM
    equipes_manutencao e
    JOIN equipe_membro em ON em.id_equipe = e.id_equipe
    JOIN pessoas p ON p.documento = em.documento
    RIGHT JOIN trechos_rodovia t ON t.id_equipe = e.id_equipe
ORDER BY e.id_equipe, t.id_trecho;

SELECT * FROM relatorio_prioridade;