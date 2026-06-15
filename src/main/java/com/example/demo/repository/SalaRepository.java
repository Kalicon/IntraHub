package com.example.demo.repository;
import com.example.demo.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SalaRepository extends JpaRepository<Sala, Long> {
    List<Sala> findByAtivaTrue();
}