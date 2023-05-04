package com.example.miniautorizador.exceptions;


import com.example.miniautorizador.domain.dtos.CartaoDTO;
import lombok.Getter;

@Getter
public class CartaoExistenteException extends RuntimeException{

    private final CartaoDTO cartao;


    public CartaoExistenteException(CartaoDTO cartao) {
        super("O cartão já existe");
        this.cartao = cartao;
    };
}
