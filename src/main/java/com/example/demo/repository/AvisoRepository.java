package com.example.demo.repository;

import com.example.demo.model.Aviso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AvisoRepository extends JpaRepository<Aviso, Long> {
    // Ordena para mostrar os mais novos primeiro
    List<Aviso> findAllByOrderByDataPostagemDesc();
}