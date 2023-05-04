package com.example.miniautorizador.Integration;

import com.example.miniautorizador.domain.Cartao;
import com.example.miniautorizador.domain.dtos.CartaoDTO;
import com.example.miniautorizador.repositories.CartoesRepositorie;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("teste")
public class CartoesControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CartoesRepositorie cartoesRepositorie;

    private final String NUMERO_CARTAO = "123456789";
    private final String SENHA = "senha";

    private CartaoDTO cartaoDTO;

    @BeforeEach
    void setup() {
        cartaoDTO = new CartaoDTO();
        cartaoDTO.setNumeroCartao(NUMERO_CARTAO);
        cartaoDTO.setSenha(SENHA);
    }

    @AfterEach
    void tearDown() {
        cartoesRepositorie.deleteAll();
        assertThat(0).isEqualTo(cartoesRepositorie.count());
    }


    @Test
    @DisplayName("Deve criar e salvar um cartão sem erros")
    void deveCriarCartaoSemErros() throws Exception{

        mockMvc.perform(post("/cartoes").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartaoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroCartao").value(NUMERO_CARTAO))
                .andExpect(jsonPath("$.senha").value(SENHA));

        assertThat(1).isEqualTo(cartoesRepositorie.count());
    }

    @Test
    @DisplayName("Deve criar retornar erro cartão já existe")
    void deveRetornarErrorCartaoExistente() throws Exception{

        cartoesRepositorie.save(new Cartao(NUMERO_CARTAO, SENHA));

        mockMvc.perform(post("/cartoes").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartaoDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("O cartão já existe"))
                .andExpect(jsonPath("$.corpo.numeroCartao").value(NUMERO_CARTAO))
                .andExpect(jsonPath("$.corpo.senha").value(SENHA));

        assertThat(1).isEqualTo(cartoesRepositorie.count());
    }

    @Test
    @DisplayName("Deve retornar o saldo corretamente")
    void deveRetornarSaldoSemErros() throws Exception{

        cartoesRepositorie.save(new Cartao(NUMERO_CARTAO, SENHA));

        mockMvc.perform(get("/cartoes/".concat(NUMERO_CARTAO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("500.00"));
    }

    @Test
    @DisplayName("Deve retornar erro cartao nao encontrado")
    void deveRetornarErroCartaoNaoEncontrado() throws Exception{

        mockMvc.perform(get("/cartoes/".concat("1321561")).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
