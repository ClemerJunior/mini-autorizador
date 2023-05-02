package com.example.miniautorizador.service;


import com.example.miniautorizador.domain.Cartao;
import com.example.miniautorizador.domain.dtos.CartaoDTO;
import com.example.miniautorizador.repositories.CartoesRepositorie;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CartoesServiceTest {

    private CartoesService cartoesService;

    @Mock
    private CartoesRepositorie cartoesRepositorie;

    @BeforeEach
    void setup() {
        cartoesService = new CartoesService(cartoesRepositorie);
    }


    @Test
    @DisplayName("Deve criar um cartão novo sem erros")
    void shoudCreateNewCard() {
        CartaoDTO cartaoDTO = new CartaoDTO();
        cartaoDTO.setNumeroCartao("123456313");
        cartaoDTO.setSenha("senha");

        Cartao cartao = new Cartao(cartaoDTO.getNumeroCartao(), cartaoDTO.getSenha());

        Mockito.when(cartoesRepositorie.save(any())).thenReturn(cartao);
        CartaoDTO cartaoCriado = cartoesService.criarCartao(cartaoDTO);

        Assertions.assertThat(cartaoCriado.getNumeroCartao()).isEqualTo(cartao.getNumeroCartao());
        Assertions.assertThat(cartaoCriado.getSenha()).isEqualTo(cartao.getSenha());
        Assertions.assertThat(cartaoCriado.getSaldo()).isEqualTo(new BigDecimal("500"));
    }

    @Test
    @DisplayName("Deve criar um cartão novo sem erros")
    void shoudCreateNewCardf() {
        CartaoDTO cartaoDTO = new CartaoDTO();
        cartaoDTO.setNumeroCartao("123456313");
        cartaoDTO.setSenha("senha");

        Cartao cartao = new Cartao(cartaoDTO.getNumeroCartao(), cartaoDTO.getSenha());
        cartao.setSaldo(new BigDecimal("-1"));

        Mockito.when(cartoesRepositorie.save(any())).thenReturn(cartao);
        CartaoDTO cartaoCriado = cartoesService.criarCartao(cartaoDTO);

        Assertions.assertThat(cartaoCriado.getNumeroCartao()).isEqualTo(cartao.getNumeroCartao());
        Assertions.assertThat(cartaoCriado.getSenha()).isEqualTo(cartao.getSenha());
        Assertions.assertThat(cartaoCriado.getSaldo()).isEqualTo(new BigDecimal("500"));
    }
}
