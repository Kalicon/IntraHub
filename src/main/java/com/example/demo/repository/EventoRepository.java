package com.example.demo.repository;

import com.example.demo.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    // Ordena para mostrar os eventos mais próximos/novos primeiro
    List<Evento> findAllByOrderByDataEventoDesc();
}