package com.example.miniautorizador.Integration;

import com.example.miniautorizador.domain.Cartao;
import com.example.miniautorizador.domain.dtos.TransacaoDTO;
import com.example.miniautorizador.domain.enumerations.TransacaoStatusEnum;
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

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("teste")
public class TransacaoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CartoesRepositorie cartoesRepositorie;

    private final String NUMERO_CARTAO = "123456789";

    private TransacaoDTO transacao;

    @BeforeEach
    void setup() {
        transacao = new TransacaoDTO();
        transacao.setNumeroCartao(NUMERO_CARTAO);
        transacao.setSenhaCartao("senha");
        transacao.setValor(new BigDecimal("200"));

        Cartao cartao = new Cartao(NUMERO_CARTAO, "senha");
        cartoesRepositorie.save(cartao);
    }

    @AfterEach
    void tearDown() {
        cartoesRepositorie.deleteAll();
        assertThat(0).isEqualTo(cartoesRepositorie.count());
    }

    @Test
    @DisplayName("Deve realizar uma transação sem erros e atualizar o saldo do cartao")
    void deveCriarUmaTransacaoEAtualizarSaldo() throws Exception {

        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transacao)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(TransacaoStatusEnum.OK)));

        assertThat(new BigDecimal("300"))
                .isEqualByComparingTo(cartoesRepositorie
                        .getCartaoByNumeroCartao(NUMERO_CARTAO)
                        .get()
                        .getSaldo());
    }

    @Test
    @DisplayName("Deve realizar uma transação e retornar erro cartao inexistente")
    void deveRetornarErroCartaoInexistente() throws Exception {

        transacao.setNumeroCartao("131531315113");

        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transacao)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("422"))
                .andExpect(jsonPath("$.message").value(TransacaoStatusEnum.CARTAO_INEXISTENTE.name()));

        assertThat(cartoesRepositorie.getCartaoByNumeroCartao(NUMERO_CARTAO).get().getSaldo())
                .isEqualByComparingTo(new BigDecimal("500"));
    }

    @Test
    @DisplayName("Deve realizar uma transação e retornar erro senha invalida")
    void deveRetornarErroSenhaInvalida() throws Exception {

        transacao.setSenhaCartao("senhaErrada");

        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transacao)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("422"))
                .andExpect(jsonPath("$.message").value(TransacaoStatusEnum.SENHA_INVALIDA.name()));

        assertThat(new BigDecimal("500"))
                .isEqualByComparingTo(cartoesRepositorie
                        .getCartaoByNumeroCartao(NUMERO_CARTAO)
                        .get()
                        .getSaldo());
    }

    @Test
    @DisplayName("Deve realizar uma transação e retornar erro saldo insuficiente")
    void deveRetornarErroSaldoInsuficiente() throws Exception {

        transacao.setValor(new BigDecimal("550"));

        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transacao)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("422"))
                .andExpect(jsonPath("$.message").value(TransacaoStatusEnum.SALDO_INSUFICIENTE.name()));

        assertThat(new BigDecimal("500"))
                .isEqualByComparingTo(cartoesRepositorie
                        .getCartaoByNumeroCartao(NUMERO_CARTAO)
                        .get()
                        .getSaldo());
    }
}
