package com.example.miniautorizador.handlers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseErro {

    private int code;
    private String message;
    private Object corpo;
}
