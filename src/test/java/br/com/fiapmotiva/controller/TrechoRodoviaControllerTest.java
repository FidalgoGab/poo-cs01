package br.com.fiapmotiva.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TrechoRodoviaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveListarTrechosComSucesso() throws Exception {
        mockMvc.perform(get("/api/trechos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deveCriarTrechoComSucesso() throws Exception {
        String json = """
                {
                    "nomeTrecho": "BR-116 KM 10 a 25",
                    "kmInicial": 10.0,
                    "kmFinal": 25.0,
                    "nivelVegetacaoCm": 35.5,
                    "tipoTrecho": "UMIDO",
                    "monitoravelIot": 1
                }
                """;

        mockMvc.perform(post("/api/trechos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idTrecho").exists())
                .andExpect(jsonPath("$.nomeTrecho").value("BR-116 KM 10 a 25"))
                .andExpect(jsonPath("$.extensaoKm").value(15.0));
    }

    @Test
    void deveRetornarBadRequestQuandoKmFinalMenorOuIgualKmInicial() throws Exception {
        String json = """
                {
                    "nomeTrecho": "Trecho Invalido",
                    "kmInicial": 50.0,
                    "kmFinal": 20.0,
                    "nivelVegetacaoCm": 10.0,
                    "tipoTrecho": "SECO",
                    "monitoravelIot": 0
                }
                """;

        mockMvc.perform(post("/api/trechos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void deveRetornarNotFoundAoBuscarIdInexistente() throws Exception {
        mockMvc.perform(get("/api/trechos/99999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
