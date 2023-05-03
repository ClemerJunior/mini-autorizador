package com.example.miniautorizador.controller;

import com.example.miniautorizador.domain.dtos.CartaoDTO;
import com.example.miniautorizador.exceptions.CartaoExistenteException;
import com.example.miniautorizador.service.CartoesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartoesController.class)
public class CartoesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartoesService cartoesService;

    @Test
    @DisplayName("Requisição de criação do cartão correta, retornando 201")
    void whenValidThenReturn201AndCreate() throws Exception {
        CartaoDTO cartaoDTO = new CartaoDTO();
        cartaoDTO.setNumeroCartao("1234564789");
        cartaoDTO.setSenha("senha");

        when(cartoesService.criarCartao(any())).thenReturn(cartaoDTO);

        mockMvc.perform(post("/cartoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartaoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroCartao").value("1234564789"))
                .andExpect(jsonPath("$.senha").value("senha"));

        verify(cartoesService, times(1)).criarCartao(any());
    }

    @Test
    @DisplayName("Deve retornar 400 quando o numero do cartao invalido ou for nulo")
    void whenInvalidRequestNumeroCartaoNull() throws Exception {
        CartaoDTO cartaoDTO = new CartaoDTO();
        cartaoDTO.setNumeroCartao(null);
        cartaoDTO.setSenha("senha");

        mockMvc.perform(post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartaoDTO)))
                .andExpect(status().isBadRequest());

        verify(cartoesService, times(0)).criarCartao(any());

        cartaoDTO.setNumeroCartao("");

        mockMvc.perform(post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartaoDTO)))
                .andExpect(status().isBadRequest());

        verify(cartoesService, times(0)).criarCartao(any());

        cartaoDTO.setNumeroCartao("asdf15861");

        mockMvc.perform(post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartaoDTO)))
                .andExpect(status().isBadRequest());

        verify(cartoesService, times(0)).criarCartao(any());
    }

    @Test
    @DisplayName("Deve retornar 400 quando a senha for nula ou vazia")
    void whenInvalidRequestSenhaNull() throws Exception {
        CartaoDTO cartaoDTO = new CartaoDTO();
        cartaoDTO.setNumeroCartao("1234569789");
        cartaoDTO.setSenha(null);

        mockMvc.perform(post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartaoDTO)))
                .andExpect(status().isBadRequest());

        verify(cartoesService, times(0)).criarCartao(any());

        cartaoDTO.setSenha("");

        mockMvc.perform(post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartaoDTO)))
                .andExpect(status().isBadRequest());

        verify(cartoesService, times(0)).criarCartao(any());
    }

    @Test
    @DisplayName("Deve retornar o cartao Criado com numero e senha corretamente")
    void whenCreatedCorrectResponse() throws Exception{
        CartaoDTO cartaoDTO = new CartaoDTO();
        cartaoDTO.setNumeroCartao("1234569789");
        cartaoDTO.setSenha("senha");

        when(cartoesService.criarCartao(any())).thenReturn(cartaoDTO);

        MvcResult mvcResult = mockMvc.perform(post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartaoDTO)))
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(responseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(cartaoDTO));

        verify(cartoesService, times(1)).criarCartao(any());
    }

    @Test
    @DisplayName("Deve retornar erro de cartão já existente com código 422")
    void whenCreateCartaoJaExisteResponse() throws Exception{
        CartaoDTO cartaoDTO = new CartaoDTO();
        cartaoDTO.setNumeroCartao("1234569789");
        cartaoDTO.setSenha("senha");

        when(cartoesService.criarCartao(any())).thenThrow(new CartaoExistenteException(any()));

        mockMvc.perform(post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartaoDTO)))
                .andExpect(status().isUnprocessableEntity());

        verify(cartoesService, times(1)).criarCartao(any());
    }

    @Test
    @DisplayName("Deve retornar o saldo do cartao corretamente")
    void shouldReturnSaldo() throws Exception {
        when(cartoesService.consultarSaldo(any())).thenReturn(new BigDecimal("500"));

        MvcResult mvcResult = mockMvc.perform(get("/cartoes/1231456789")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();

        Assertions.assertThat(new BigDecimal("500")).isEqualTo(new BigDecimal(responseBody));

        verify(cartoesService, times(1)).consultarSaldo(any());
    }

    @Test
    @DisplayName("Deve retornar 400 quando o numero do cartao for invalido")
    void whenNumeroCartaoInvalid() throws Exception{
        mockMvc.perform(get("/cartoes/asdf121313")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(cartoesService, times(0)).criarCartao(any());
    }
}
