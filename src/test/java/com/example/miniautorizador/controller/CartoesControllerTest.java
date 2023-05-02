package com.example.miniautorizador.controller;

import com.example.miniautorizador.domain.dtos.CartaoDTO;
import com.example.miniautorizador.exceptions.CartaoExistenteException;
import com.example.miniautorizador.service.CartoesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(CartoesController.class)
public class CartoesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartoesService cartoesService;

    @Test
    @DisplayName("Requisição de criação do cartão correta, retornando 200")
    void whenValidThenReturn200AndCreate() throws Exception {
        CartaoDTO cartaoDTO = new CartaoDTO();
        cartaoDTO.setNumeroCartao("1234564789");
        cartaoDTO.setSenha("senha");
        cartaoDTO.setSaldo(new BigDecimal("-1"));

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartaoDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("Deve retornar 400 quando o numero do cartao invalido for nulo")
    void whenInvalidRequestNumeroCartaoNull() throws Exception {
        CartaoDTO cartaoDTO = new CartaoDTO();
        cartaoDTO.setNumeroCartao(null);
        cartaoDTO.setSenha("senha");

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartaoDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        cartaoDTO.setNumeroCartao("");

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartaoDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        cartaoDTO.setNumeroCartao("asdf15861");

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartaoDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 400 quando o numero do cartao for vazio")
    void whenInvalidRequestNumeroCartaoEmpty() throws Exception {
        CartaoDTO cartaoDTO = new CartaoDTO();
        cartaoDTO.setNumeroCartao("");
        cartaoDTO.setSenha("senha");

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartaoDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 400 quando o numero do cartao invalido")
    void whenInvalidRequestNumeroCartaoInvalid() throws Exception {
        CartaoDTO cartaoDTO = new CartaoDTO();
        cartaoDTO.setNumeroCartao("1321asdfffa13215");
        cartaoDTO.setSenha("senha");

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartaoDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 400 quando a senha for nula")
    void whenInvalidRequestSenhaNull() throws Exception {
        CartaoDTO cartaoDTO = new CartaoDTO();
        cartaoDTO.setNumeroCartao("1234569789");
        cartaoDTO.setSenha(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartaoDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 400 quando a senha for vazia")
    void whenInvalidRequestSenhaEmpty() throws Exception {
        CartaoDTO cartaoDTO = new CartaoDTO();
        cartaoDTO.setNumeroCartao("1234569789");
        cartaoDTO.setSenha("");

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartaoDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar o cartao Criado com numero e senha corretamente")
    void whenCreatedCorrectResponse() throws Exception{
        CartaoDTO cartaoDTO = new CartaoDTO();
        cartaoDTO.setNumeroCartao("1234569789");
        cartaoDTO.setSenha("senha");

        Mockito.when(cartoesService.criarCartao(any())).thenReturn(cartaoDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartaoDTO)))
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(responseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(cartaoDTO));

    }

    @Test
    @DisplayName("Deve retornar erro de cartão já existente com código 422")
    void whenCreateCartaoJaExisteResponse() throws Exception{
        CartaoDTO cartaoDTO = new CartaoDTO();
        cartaoDTO.setNumeroCartao("1234569789");
        cartaoDTO.setSenha("senha");

        Mockito.when(cartoesService.criarCartao(any())).thenThrow(new CartaoExistenteException(cartaoDTO));

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartaoDTO)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }
}
