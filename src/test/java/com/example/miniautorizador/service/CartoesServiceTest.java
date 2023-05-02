package com.example.miniautorizador.service;


import com.example.miniautorizador.domain.Cartao;
import com.example.miniautorizador.domain.dtos.CartaoDTO;
import com.example.miniautorizador.exceptions.CartaoExistenteException;
import com.example.miniautorizador.exceptions.CartaoInexistenteException;
import com.example.miniautorizador.repositories.CartoesRepositorie;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CartoesServiceTest {

    private CartoesService cartoesService;

    @Mock
    private CartoesRepositorie cartoesRepositorie;

    private CartaoDTO cartaoDTO;

    @BeforeEach
    void setup() {
        cartoesService = new CartoesService(cartoesRepositorie);
        cartaoDTO = new CartaoDTO();
        cartaoDTO.setNumeroCartao("123456313");
        cartaoDTO.setSenha("senha");

    }


    @Test
    @DisplayName("Deve criar um cartão novo sem erros")
    void shouldCreateNewCard() {
        Cartao cartao = new Cartao(cartaoDTO.getNumeroCartao(), cartaoDTO.getSenha());

        Mockito.when(cartoesRepositorie.save(any())).thenReturn(cartao);
        CartaoDTO cartaoCriado = cartoesService.criarCartao(cartaoDTO);

        Assertions.assertThat(cartaoCriado.getNumeroCartao()).isEqualTo(cartao.getNumeroCartao());
        Assertions.assertThat(cartaoCriado.getSenha()).isEqualTo(cartao.getSenha());
    }

    @Test
    @DisplayName("Deve lançar uma exceção com o cartão já existente")
    void shouldThrowCartaoExistenteException() {
        Mockito.when(cartoesRepositorie.save(any())).thenThrow(new DataIntegrityViolationException(""));

        Assertions.assertThatThrownBy(() -> cartoesService.criarCartao(cartaoDTO))
                .isInstanceOf(CartaoExistenteException.class)
                .hasMessage("O cartão já existe");
    }

    @Test
    @DisplayName("Deve retornar o saldo do cartao consultado pelo numero")
    void shouldReturnSaldo() {
        Mockito.when(cartoesRepositorie.getCartaoByNumeroCartao(any()))
                .thenReturn(Optional.of(new Cartao(cartaoDTO.getNumeroCartao(), cartaoDTO.getSenha())));

        BigDecimal saldo = cartoesService.consultarSaldo("123456313");

        Assertions.assertThat(saldo).isEqualTo(new BigDecimal("500"));
    }

    @Test
    @DisplayName("Deve retornar 404 cartão não encontrado")
    void shouldReturnNotFound() {
        Mockito.when(cartoesRepositorie.getCartaoByNumeroCartao(any())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> cartoesService.consultarSaldo("123456313"))
                .isInstanceOf(CartaoInexistenteException.class)
                .hasMessage("cartão não encontrado");
    }
}
