package com.example.miniautorizador.handlers;

import com.example.miniautorizador.exceptions.CartaoExistenteException;
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


}
