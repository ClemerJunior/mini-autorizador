package com.example.miniautorizador.service;

import com.example.miniautorizador.domain.Cartao;
import com.example.miniautorizador.domain.dtos.TransacaoDTO;
import com.example.miniautorizador.domain.enumerations.TransacaoStatusEnum;
import com.example.miniautorizador.exceptions.CartaoInexistenteException;
import com.example.miniautorizador.exceptions.SaldoInsuficienteException;
import com.example.miniautorizador.exceptions.SenhaInvalidaException;
import com.example.miniautorizador.repositories.CartoesRepositorie;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

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
        Mockito.when(cartoesRepositorie.getCartaoByNumeroCartao("123456789")).thenReturn(Optional.of(cartao));

        TransacaoStatusEnum status = transacaoService.realizarTransacao(transacao);

        Assertions.assertThat(status).isEqualTo(TransacaoStatusEnum.OK);
        Assertions.assertThat(cartao.getSaldo()).isEqualByComparingTo("490");
        Mockito.verify(cartoesRepositorie, VerificationModeFactory.times(1))
                .getCartaoByNumeroCartao("123456789");
        Mockito.verify(cartoesRepositorie, VerificationModeFactory.times(1))
                .save(cartao);
    }

    @Test
    @DisplayName("Deve lançar exceção cartao inexistente")
    void shouldThrowCartaoInexistenteException() {
        Mockito.when(cartoesRepositorie.getCartaoByNumeroCartao(any())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> transacaoService.realizarTransacao(transacao))
                .isInstanceOf(CartaoInexistenteException.class)
                .hasMessage(TransacaoStatusEnum.CARTAO_INEXISTENTE.name());
        Assertions.assertThat(cartao.getSaldo()).isEqualByComparingTo("500");
        Mockito.verify(cartoesRepositorie, VerificationModeFactory.times(1))
                .getCartaoByNumeroCartao(any());
        Mockito.verify(cartoesRepositorie, VerificationModeFactory.times(0))
                .save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção Senha inválida")
    void shouldThrowSenhaInvalidaException() {
        Mockito.when(cartoesRepositorie.getCartaoByNumeroCartao(cartao.getNumeroCartao())).thenReturn(Optional.of(cartao));
        transacao.setSenhaCartao("1321564");

        Assertions.assertThatThrownBy(() -> transacaoService.realizarTransacao(transacao))
                .isInstanceOf(SenhaInvalidaException.class)
                .hasMessage(TransacaoStatusEnum.SENHA_INVALIDA.name());
        Assertions.assertThat(cartao.getSaldo()).isEqualByComparingTo("500");
        Mockito.verify(cartoesRepositorie, VerificationModeFactory.times(1))
                .getCartaoByNumeroCartao(any());
        Mockito.verify(cartoesRepositorie, VerificationModeFactory.times(0))
                .save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção Saldo Insuficiente")
    void shouldThrowSaldoInsuficienteException() {
        Mockito.when(cartoesRepositorie.getCartaoByNumeroCartao(cartao.getNumeroCartao())).thenReturn(Optional.of(cartao));
        transacao.setValor(new BigDecimal("550"));

        Assertions.assertThatThrownBy(() -> transacaoService.realizarTransacao(transacao))
                .isInstanceOf(SaldoInsuficienteException.class)
                .hasMessage(TransacaoStatusEnum.SALDO_INSUFICIENTE.name());
        Assertions.assertThat(cartao.getSaldo()).isEqualByComparingTo("500");
        Mockito.verify(cartoesRepositorie, VerificationModeFactory.times(1))
                .getCartaoByNumeroCartao(any());
        Mockito.verify(cartoesRepositorie, VerificationModeFactory.times(0))
                .save(any());
    }
}
