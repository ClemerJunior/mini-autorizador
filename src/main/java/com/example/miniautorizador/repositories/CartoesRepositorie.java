package com.example.miniautorizador.repositories;

import com.example.miniautorizador.domain.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface CartoesRepositorie extends JpaRepository<Cartao, Long> {

    public Cartao getCartaoByNumeroCartao(String numeroCartao);

}
