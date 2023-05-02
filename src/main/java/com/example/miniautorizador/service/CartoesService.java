package com.example.miniautorizador.service;

import com.example.miniautorizador.domain.Cartao;
import com.example.miniautorizador.domain.dtos.CartaoDTO;
import com.example.miniautorizador.exceptions.CartaoExistenteException;
import com.example.miniautorizador.exceptions.CartaoInexistenteException;
import com.example.miniautorizador.repositories.CartoesRepositorie;
import lombok.RequiredArgsConstructor;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CartoesService {

    private final CartoesRepositorie cartoesRepositorie;

    public CartaoDTO criarCartao(CartaoDTO cartaoDTO) {

        Cartao cartaoCriado;

        try {
            cartaoCriado = cartoesRepositorie.save(new Cartao(cartaoDTO.getNumeroCartao(), cartaoDTO.getSenha()));
        } catch (DataIntegrityViolationException ex) {
            throw new CartaoExistenteException(cartaoDTO);
        }

        ModelMapper mapper = new ModelMapper();
        return mapper.map(cartaoCriado, CartaoDTO.class);
    }

    public BigDecimal consultarSaldo(String numeroCartao) {
        return cartoesRepositorie.getCartaoByNumeroCartao(numeroCartao)
                .orElseThrow(CartaoInexistenteException::new).getSaldo();
    }
}
