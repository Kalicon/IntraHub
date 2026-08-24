package com.example.demo.repository.health;

import com.example.demo.model.health.Leito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeitoRepository extends JpaRepository<Leito, Long> {
    List<Leito> findBySetor(String setor);
    List<Leito> findByStatus(String status);
    Optional<Leito> findByCodigo(String codigo);
    long countByStatus(String status);
}