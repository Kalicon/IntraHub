package com.example.demo.repository; // <--- O ENDEREÇO CORRETO

import com.example.demo.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<Link, Long> {
    // Nenhuma regra extra necessária por enquanto
}