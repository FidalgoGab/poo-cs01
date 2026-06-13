package br.com.fiapmotiva.model;

/**
 * Interface MonitoravelViaIoT
 * 
 * Contrato de comportamento desacoplado de hierarquia. Qualquer classe que possa transmitir dados
 * via sensores IoT implementa esta interface.
 * 
 * Pergunta de Reflexão: Qual a diferença arquitetural entre fazer um Trecho herdar de uma classe abstrata vs. implementar uma Interface?
 * Resposta:
 * - A Herança de classe abstrata estabelece uma relação de "é um" e compartilha implementação de código, 
 *   atributos e estado. Um trecho herda a estrutura básica de TrechoRodovia.
 * - A Interface estabelece um contrato de comportamento "pode fazer" desacoplado, sem impor uma hierarquia rígida
 *   ou compartilhar estrutura. Isso permite que outras entidades (como veículos, drones ou
 *   postes de iluminação) também sejam monitoráveis via IoT sem herdar de TrechoRodovia.
 */
public interface MonitoravelViaIoT {
    double transmitirDadosSensor();
}
