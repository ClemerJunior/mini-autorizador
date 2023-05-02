package com.example.miniautorizador.handlers;

import com.example.miniautorizador.domain.enumerations.TransacaoStatusEnum;
import com.example.miniautorizador.exceptions.CartaoExistenteException;
import com.example.miniautorizador.exceptions.CartaoInexistenteException;
import com.example.miniautorizador.exceptions.SaldoInsuficienteException;
import com.example.miniautorizador.exceptions.SenhaInvalidaException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandle {

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(CartaoExistenteException.class)
    public ResponseErro handleCartaoExistenteException(CartaoExistenteException ex) {

        ResponseErro responseErro = new ResponseErro();
        responseErro.setCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
        responseErro.setMessage(ex.getMessage());
        responseErro.setCorpo(ex.getCartao());

        return responseErro;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CartaoInexistenteException.class)
    public ResponseErro handleCartaoInexistenteException(CartaoInexistenteException ex) {

        ResponseErro responseErro = new ResponseErro();
        responseErro.setCode(HttpStatus.NOT_FOUND.value());
        responseErro.setMessage(ex.getMessage());

        return responseErro;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseErro handleConstraintViolationException(ConstraintViolationException ex) {

        ResponseErro responseErro = new ResponseErro();
        responseErro.setCode(HttpStatus.BAD_REQUEST.value());
        responseErro.setMessage(ex.getMessage());

        return responseErro;
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(SenhaInvalidaException.class)
    public ResponseErro handleSenhaInvalidaException(SenhaInvalidaException ex) {

        ResponseErro responseErro = new ResponseErro();
        responseErro.setCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
        responseErro.setMessage(TransacaoStatusEnum.SENHA_INVALIDA.name());

        return responseErro;
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseErro handleSaldoInsuficienteException(SaldoInsuficienteException ex) {

        ResponseErro responseErro = new ResponseErro();
        responseErro.setCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
        responseErro.setMessage(TransacaoStatusEnum.SALDO_INSUFICIENTE.name());

        return responseErro;
    }
}
