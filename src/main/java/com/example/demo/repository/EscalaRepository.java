package com.example.demo.repository;

import com.example.demo.model.Escala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface EscalaRepository extends JpaRepository<Escala, Long> {
    Optional<Escala> findByData(LocalDate data);
}