package com.example.miniautorizador.controller;

import com.example.miniautorizador.domain.dtos.CartaoDTO;
import com.example.miniautorizador.service.CartoesService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/cartoes")
@RequiredArgsConstructor
@Validated
public class CartoesController {

    private final CartoesService cartoesService;

    @PostMapping
    public ResponseEntity<CartaoDTO> criarCartao(@Valid @RequestBody CartaoDTO cartaoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartoesService.criarCartao(cartaoDTO));
    }

    @GetMapping("/{numeroCartao}")
    public ResponseEntity<BigDecimal> consultarSaldo(@PathVariable("numeroCartao") @Pattern(regexp = "^[0-9]+$") String numeroCartao) {
        return ResponseEntity.ok(cartoesService.consultarSaldo(numeroCartao));
    }
}
