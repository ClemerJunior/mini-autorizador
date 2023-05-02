package com.example.miniautorizador.service;

import com.example.miniautorizador.domain.Cartao;
import com.example.miniautorizador.domain.dtos.CartaoDTO;
import com.example.miniautorizador.exceptions.CartaoExistenteException;
import com.example.miniautorizador.repositories.CartoesRepositorie;
import lombok.RequiredArgsConstructor;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartoesService {

    private final CartoesRepositorie cartoesRepositorie;

    public CartaoDTO criarCartao(CartaoDTO cartaoDTO) {
        ModelMapper mapper = new ModelMapper();
        Cartao cartao = new Cartao(cartaoDTO.getNumeroCartao(), cartaoDTO.getSenha());

        try {
            cartao = cartoesRepositorie.save(cartao);
        } catch (DataIntegrityViolationException ex) {
            throw new CartaoExistenteException(cartaoDTO);
        }

        return mapper.map(cartao, CartaoDTO.class);
    }
}
