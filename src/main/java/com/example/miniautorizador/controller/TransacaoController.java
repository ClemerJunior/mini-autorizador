package com.example.miniautorizador.controller;

import com.example.miniautorizador.domain.dtos.TransacaoDTO;
import com.example.miniautorizador.domain.enumerations.TransacaoStatusEnum;
import com.example.miniautorizador.service.TransacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacoes")
@RequiredArgsConstructor
public class TransacaoController {

    private final TransacaoService transacaoService;

    @PostMapping
    public ResponseEntity<TransacaoStatusEnum> realizarTransacao(@Valid @RequestBody TransacaoDTO transacao) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transacaoService.realizarTransacao(transacao));
    }
}
