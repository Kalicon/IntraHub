package com.example.demo.repository;

import com.example.demo.model.Enquete;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EnqueteRepository extends JpaRepository<Enquete, Long> {
    // Busca a primeira enquete marcada como ativa
    Optional<Enquete> findFirstByAtivaTrueOrderByIdDesc();
}