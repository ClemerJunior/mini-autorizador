package com.example.miniautorizador.domain.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class CartaoDTO {

    private Long numeroCartao;
    private BigDecimal saldo;
    private String senha;
}
