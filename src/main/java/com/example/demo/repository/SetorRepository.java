package com.example.demo.repository; // <--- O IMPORTANTE ESTÁ AQUI: .repository

import com.example.demo.model.Setor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SetorRepository extends JpaRepository<Setor, Long> {

    // Busca um setor pelo nome (Ex: "UTI", "Recepção")
    Setor findByNome(String nome);
}