package com.example.miniautorizador.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CartaoDTO {

    @NotBlank
    @Pattern(regexp = "^[0-9]+$")
    private String numeroCartao;

    @NotBlank
    private String senha;
}
