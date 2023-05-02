package com.example.miniautorizador.exceptions;


import com.example.miniautorizador.domain.dtos.CartaoDTO;
import lombok.Getter;

@Getter
public class CartaoExistenteException extends RuntimeException{

    private final CartaoDTO cartao;
    private final String message;


    public CartaoExistenteException(CartaoDTO cartao) {
        this.cartao = cartao;
        this.message = "O Cartão já existe";
    };
}
