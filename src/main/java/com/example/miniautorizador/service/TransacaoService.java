package com.example.miniautorizador.service;

import com.example.miniautorizador.domain.Cartao;
import com.example.miniautorizador.domain.dtos.TransacaoDTO;
import com.example.miniautorizador.domain.enumerations.TransacaoStatusEnum;
import com.example.miniautorizador.exceptions.CartaoInexistenteException;
import com.example.miniautorizador.exceptions.SaldoInsuficienteException;
import com.example.miniautorizador.exceptions.SenhaInvalidaException;
import com.example.miniautorizador.repositories.CartoesRepositorie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final CartoesRepositorie cartoesRepositorie;

    @Transactional
    public TransacaoStatusEnum realizarTransacao(TransacaoDTO transacao) {

        Cartao cartao = cartoesRepositorie.getCartaoByNumeroCartao(transacao.getNumeroCartao())
                .orElseThrow(CartaoInexistenteException::new);
        autorizarTransacao(cartao, transacao);
        cartao.setSaldo(cartao.getSaldo().subtract(transacao.getValor()));
        cartoesRepositorie.save(cartao);

        return TransacaoStatusEnum.OK;
    }

    private void autorizarTransacao(Cartao cartao, TransacaoDTO transacao) {
          Optional.of(cartao)
                  .filter(c -> c.getSenha().equals(transacao.getSenhaCartao()))
                  .orElseThrow(SenhaInvalidaException::new);

          Optional.of(cartao)
                  .filter(c -> c.getSaldo().subtract(transacao.getValor()).compareTo(BigDecimal.ZERO) >= 0)
                  .orElseThrow(SaldoInsuficienteException::new);
    }
}
