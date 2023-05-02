package com.example.miniautorizador.exceptions;

import com.example.miniautorizador.domain.enumerations.TransacaoStatusEnum;

public class CartaoInexistenteException extends RuntimeException{

    private String message;

    public CartaoInexistenteException() {
        super(TransacaoStatusEnum.CARTAO_INEXISTENTE.name());
    }

}
