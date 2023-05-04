package com.example.miniautorizador.repositories;

import com.example.miniautorizador.domain.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartoesRepositorie extends JpaRepository<Cartao, Long> {

    Optional<Cartao> getCartaoByNumeroCartao(String numeroCartao);
}
