package com.example.miniautorizador.controller;

import com.example.miniautorizador.domain.dtos.TransacaoDTO;
import com.example.miniautorizador.domain.enumerations.TransacaoStatusEnum;
import com.example.miniautorizador.exceptions.AutorizacaoException;
import com.example.miniautorizador.service.TransacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransacaoController.class)
public class TransacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    TransacaoService transacaoService;

    @Test
    @DisplayName("Deve realizar a requisição da transação corretamente e retornar o codigo 201")
    void whenValidReturn201Ok() throws Exception {
        TransacaoDTO transacao = new TransacaoDTO();
        transacao.setNumeroCartao("123456789");
        transacao.setSenhaCartao("senha");
        transacao.setValor(new BigDecimal("50"));

        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(transacao)))
                .andExpect(status().isCreated());
    }


    @Test
    @DisplayName("Deve realizar a requisição da transação e retornar 422 com a mensagem correta Cartão Inexistente")
    void whenCartaoInexistenteReturn422AndCorrectMessage() throws Exception {
        TransacaoDTO transacao = new TransacaoDTO();
        transacao.setNumeroCartao("123456789");
        transacao.setSenhaCartao("senha");
        transacao.setValor(new BigDecimal("50"));

        Mockito.when(transacaoService.realizarTransacao(any()))
                .thenThrow(new AutorizacaoException(TransacaoStatusEnum.CARTAO_INEXISTENTE.name()));

        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(transacao)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value(422))
                .andExpect(jsonPath("$.message").value(TransacaoStatusEnum.CARTAO_INEXISTENTE.name()));

        Mockito.verify(transacaoService, Mockito.times(1)).realizarTransacao(any());
    }

    @Test
    @DisplayName("Deve realizar a requisição da transação e retornar 422 com a mensagem correta Senha Invalida")
    void whenSenhaInvalidaReturn422AndCorrectMessage() throws Exception {
        TransacaoDTO transacao = new TransacaoDTO();
        transacao.setNumeroCartao("123456789");
        transacao.setSenhaCartao("senha");
        transacao.setValor(new BigDecimal("50"));

        Mockito.when(transacaoService.realizarTransacao(any()))
                .thenThrow(new AutorizacaoException(TransacaoStatusEnum.SENHA_INVALIDA.name()));

        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(transacao)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value(422))
                .andExpect(jsonPath("$.message").value(TransacaoStatusEnum.SENHA_INVALIDA.name()));

        Mockito.verify(transacaoService, Mockito.times(1)).realizarTransacao(any());
    }

    @Test
    @DisplayName("Deve realizar a requisição da transação e retornar 422 com a mensagem correta Saldo Insuficiente")
    void whenSaldInsuficienteReturn422AndCorrectMessage() throws Exception {
        TransacaoDTO transacao = new TransacaoDTO();
        transacao.setNumeroCartao("123456789");
        transacao.setSenhaCartao("senha");
        transacao.setValor(new BigDecimal("50"));

        Mockito.when(transacaoService.realizarTransacao(any()))
                .thenThrow(new AutorizacaoException(TransacaoStatusEnum.SALDO_INSUFICIENTE.name()));

        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(transacao)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value(422))
                .andExpect(jsonPath("$.message").value(TransacaoStatusEnum.SALDO_INSUFICIENTE.name()));

        Mockito.verify(transacaoService, Mockito.times(1)).realizarTransacao(any());
    }

}
