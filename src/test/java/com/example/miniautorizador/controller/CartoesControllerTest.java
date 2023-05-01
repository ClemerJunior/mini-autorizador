package com.example.miniautorizador.controller;

import com.example.miniautorizador.domain.dtos.CartaoDTO;
import com.example.miniautorizador.service.CartoesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
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
    @DisplayName("Requisição de criação do cartão correta, retornando 200 e com os dados do cartão criado")
    void whenValidThenReturn200AndCreate() throws Exception{
        CartaoDTO cartaoDTO = new CartaoDTO();
        cartaoDTO.setNumeroCartao("1234564789");
        cartaoDTO.setSenha("senha");
        cartaoDTO.setSaldo(new BigDecimal("500"));

        Mockito.when(cartoesService.criarCartao(any())).thenReturn(cartaoDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartaoDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
