package com.example.miniautorizador.service;

import com.example.miniautorizador.domain.Cartao;
import com.example.miniautorizador.domain.dtos.CartaoDTO;
import com.example.miniautorizador.repositories.CartoesRepositorie;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartoesService {

    private final CartoesRepositorie cartoesRepositorie;

    protected CartaoDTO criarCartao(CartaoDTO cartaoDTO) {
        ModelMapper mapper = new ModelMapper();
        Cartao cartao = new Cartao(cartaoDTO.getNumeroCartao(), cartaoDTO.getSenha());

        return mapper.map(cartoesRepositorie.save(cartao), CartaoDTO.class);
    }
}
