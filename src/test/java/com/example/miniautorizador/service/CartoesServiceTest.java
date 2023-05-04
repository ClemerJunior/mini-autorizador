package com.example.miniautorizador.service;


import com.example.miniautorizador.domain.Cartao;
import com.example.miniautorizador.domain.dtos.CartaoDTO;
import com.example.miniautorizador.domain.enumerations.TransacaoStatusEnum;
import com.example.miniautorizador.exceptions.CartaoExistenteException;
import com.example.miniautorizador.exceptions.CartaoInexistenteException;
import com.example.miniautorizador.repositories.CartoesRepositorie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

        when(cartoesRepositorie.save(any())).thenReturn(cartao);
        CartaoDTO cartaoCriado = cartoesService.criarCartao(cartaoDTO);

        assertThat(cartaoCriado.getNumeroCartao()).isEqualTo(cartao.getNumeroCartao());
        assertThat(cartaoCriado.getSenha()).isEqualTo(cartao.getSenha());

        verify(cartoesRepositorie, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve lançar uma exceção com o cartão já existente")
    void shouldThrowCartaoExistenteException() {
        when(cartoesRepositorie.save(any())).thenThrow(new DataIntegrityViolationException(""));

        assertThatThrownBy(() -> cartoesService.criarCartao(cartaoDTO))
                .isInstanceOf(CartaoExistenteException.class)
                .hasMessage("O cartão já existe");

        verify(cartoesRepositorie, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve retornar o saldo do cartao consultado pelo numero")
    void shouldReturnSaldo() {
        when(cartoesRepositorie.getCartaoByNumeroCartao(any()))
                .thenReturn(Optional.of(new Cartao(cartaoDTO.getNumeroCartao(), cartaoDTO.getSenha())));

        BigDecimal saldo = cartoesService.consultarSaldo("123456313");

        assertThat(saldo).isEqualTo(new BigDecimal("500"));

        verify(cartoesRepositorie, times(1)).getCartaoByNumeroCartao(any());
    }

    @Test
    @DisplayName("Deve retornar 404 cartão não encontrado")
    void shouldReturnNotFound() {
        when(cartoesRepositorie.getCartaoByNumeroCartao(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartoesService.consultarSaldo("123456313"))
                .isInstanceOf(CartaoInexistenteException.class)
                .hasMessage(TransacaoStatusEnum.CARTAO_INEXISTENTE.name());

        verify(cartoesRepositorie, times(0)).save(any());
    }
}
