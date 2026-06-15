package com.example.demo.repository; // <--- ONDE O ERRO COSTUMA ESTAR

import com.example.demo.model.Cardapio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CardapioRepository extends JpaRepository<Cardapio, Long> {
    // Exemplo: Buscar por dia da semana
    List<Cardapio> findByDiaSemana(String diaSemana);
}