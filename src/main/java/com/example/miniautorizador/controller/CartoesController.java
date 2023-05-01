package com.example.miniautorizador.controller;

import com.example.miniautorizador.domain.dtos.CartaoDTO;
import com.example.miniautorizador.service.CartoesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cartoes")
@RequiredArgsConstructor
public class CartoesController {

    private final CartoesService cartoesService;

    @PostMapping
    public ResponseEntity<CartaoDTO> criarCartao(@Valid @RequestBody CartaoDTO cartaoDTO) {
        return ResponseEntity.ok(cartoesService.criarCartao(cartaoDTO));
    }
}
