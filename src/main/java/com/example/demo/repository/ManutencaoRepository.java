package com.example.demo.repository;

import com.example.demo.model.Manutencao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ManutencaoRepository extends JpaRepository<Manutencao, Long> { // Tem que ser public
    List<Manutencao> findAllByOrderByDataAberturaDesc();
}