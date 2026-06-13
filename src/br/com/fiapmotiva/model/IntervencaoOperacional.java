package br.com.fiapmotiva.model;

/**
 * Classe Abstrata IntervencaoOperacional
 * 
 * Abstração pura e modelo de base para intervenções operacionais nos trechos das rodovias.
 * 
 * Pergunta de Reflexão: Por que não faz sentido para a Motiva que uma equipe execute apenas uma "Intervenção Operacional" genérica sem especificar qual é?
 * Resposta:
 * - Porque "Intervenção Operacional" é um conceito abstrato que não possui uma ação física concreta por si só.
 *   Uma equipe precisa saber quais recursos levar (máquinas para roçada, produtos para pulverização, etc.).
 *   A classe abstrata define o o que deve ser feito (executarServico), mas as classes filhas concretas definem a maneira.
 */
public abstract class IntervencaoOperacional {
    
    public abstract void executarServico(TrechoRodovia trecho);
}
