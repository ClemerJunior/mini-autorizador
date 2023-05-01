package com.example.miniautorizador.repositories;

import com.example.miniautorizador.domain.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartoesRepositorie extends JpaRepository<Cartao, Long> {
}
