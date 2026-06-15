package com.example.demo.repository;

import com.example.demo.model.Plantao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantaoRepository extends JpaRepository<Plantao, Long> {
}