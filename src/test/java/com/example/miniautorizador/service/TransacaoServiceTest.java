package com.example.miniautorizador.service;

import com.example.miniautorizador.domain.Cartao;
import com.example.miniautorizador.domain.dtos.TransacaoDTO;
import com.example.miniautorizador.domain.enumerations.TransacaoStatusEnum;
import com.example.miniautorizador.exceptions.AutorizacaoException;
import com.example.miniautorizador.repositories.CartoesRepositorie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransacaoServiceTest {

    private TransacaoService transacaoService;

    @Mock
    private CartoesRepositorie cartoesRepositorie;

    private Cartao cartao;

    private TransacaoDTO transacao;

    @BeforeEach
    void setup() {
        transacaoService = new TransacaoService(cartoesRepositorie);

        cartao = new Cartao("123456789", "senha");

        transacao = new TransacaoDTO();
        transacao.setNumeroCartao("123456789");
        transacao.setSenhaCartao("senha");
        transacao.setValor(new BigDecimal("10"));
    }

    @Test
    @DisplayName("Deve realiazer a transação sem erros e atualizar o saldo")
    void shouldExcuteTransacaoAndUpdateSaldo() {
        when(cartoesRepositorie.getCartaoByNumeroCartao("123456789")).thenReturn(Optional.of(cartao));

        TransacaoStatusEnum status = transacaoService.realizarTransacao(transacao);

        assertThat(status).isEqualTo(TransacaoStatusEnum.OK);
        assertThat(cartao.getSaldo()).isEqualByComparingTo("490");
        verify(cartoesRepositorie, times(1))
                .getCartaoByNumeroCartao("123456789");
        verify(cartoesRepositorie, times(1))
                .save(cartao);
    }

    @Test
    @DisplayName("Deve lançar exceção cartao Inexistente")
    void shouldThrowAutorizacaoExceptionCartaoInexistente() {
        when(cartoesRepositorie.getCartaoByNumeroCartao(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transacaoService.realizarTransacao(transacao))
                .isInstanceOf(AutorizacaoException.class)
                .hasMessage(TransacaoStatusEnum.CARTAO_INEXISTENTE.name());
        assertThat(cartao.getSaldo()).isEqualByComparingTo("500");
        verify(cartoesRepositorie, times(1))
                .getCartaoByNumeroCartao(any());
        verify(cartoesRepositorie, times(0))
                .save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção Senha inválida")
    void shouldThrowAutorizacaoExceptionSenhaInvalida() {
        when(cartoesRepositorie.getCartaoByNumeroCartao(cartao.getNumeroCartao())).thenReturn(Optional.of(cartao));
        transacao.setSenhaCartao("1321564");

        assertThatThrownBy(() -> transacaoService.realizarTransacao(transacao))
                .isInstanceOf(AutorizacaoException.class)
                .hasMessage(TransacaoStatusEnum.SENHA_INVALIDA.name());
        assertThat(cartao.getSaldo()).isEqualByComparingTo("500");
        verify(cartoesRepositorie, times(1))
                .getCartaoByNumeroCartao(any());
        verify(cartoesRepositorie, times(0))
                .save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção Saldo Insuficiente")
    void shouldThrowAutorizacaoExceptionSaldoInsuficiente() {
        when(cartoesRepositorie.getCartaoByNumeroCartao(cartao.getNumeroCartao())).thenReturn(Optional.of(cartao));
        transacao.setValor(new BigDecimal("550"));

        assertThatThrownBy(() -> transacaoService.realizarTransacao(transacao))
                .isInstanceOf(AutorizacaoException.class)
                .hasMessage(TransacaoStatusEnum.SALDO_INSUFICIENTE.name());
        assertThat(cartao.getSaldo()).isEqualByComparingTo("500");
        verify(cartoesRepositorie, times(1))
                .getCartaoByNumeroCartao(any());
        verify(cartoesRepositorie, times(0))
                .save(any());
    }
}
