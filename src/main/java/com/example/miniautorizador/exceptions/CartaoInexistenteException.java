package com.example.miniautorizador.exceptions;

public class CartaoInexistenteException extends RuntimeException{

    private String message;

    public CartaoInexistenteException() {
        super("cartão não encontrado");
    }

}
