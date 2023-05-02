package com.example.miniautorizador.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, updatable = false)
    private String numeroCartao;

    @PositiveOrZero
    private BigDecimal saldo;

    private String senha;

    public Cartao(String numeroCartao, String senha) {
        this.numeroCartao = numeroCartao;
        this.senha = senha;
        this.saldo = new BigDecimal("500");
    }
}

