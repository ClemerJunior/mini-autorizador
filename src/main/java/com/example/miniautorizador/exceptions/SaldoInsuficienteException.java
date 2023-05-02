package com.example.miniautorizador.exceptions;


import com.example.miniautorizador.domain.enumerations.TransacaoStatusEnum;

public class SaldoInsuficienteException extends RuntimeException{

    public SaldoInsuficienteException() {
        super(TransacaoStatusEnum.SALDO_INSUFICIENTE.toString());
    }
}
