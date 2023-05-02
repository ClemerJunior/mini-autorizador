package com.example.miniautorizador.exceptions;

import com.example.miniautorizador.domain.enumerations.TransacaoStatusEnum;

public class SenhaInvalidaException extends RuntimeException{

    public SenhaInvalidaException() {
        super(TransacaoStatusEnum.SENHA_INVALIDA.toString());
    }
}
