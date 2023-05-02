package com.example.miniautorizador.domain.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class CartaoDTO {

    @NotBlank
    @Pattern(regexp = "^[0-9]+$")
    private String numeroCartao;

    @NotBlank
    private String senha;
}
